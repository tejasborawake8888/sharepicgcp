# SharePic

SharePic is a social photo-sharing application built as a set of Spring Boot microservices. It supports user registration and login, profile management, creating and managing posts, viewing a feed, and liking posts.

> **Project status:** IBM reskilling project. The services have been developed and tested locally; verify the current repository and cloud configuration before treating the deployment instructions as production-ready.

## Table of Contents

- [Architecture](#architecture)
- [Technology Stack](#technology-stack)
- [Services and APIs](#services-and-apis)
- [Prerequisites](#prerequisites)
- [Repository Structure](#repository-structure)
- [Configuration](#configuration)
- [Run Locally](#run-locally)
- [Authentication](#authentication)
- [Image Storage](#image-storage)
- [Testing](#testing)
- [Cloud Deployment](#cloud-deployment)
- [Documentation](#documentation)
- [Security Notes](#security-notes)
- [Known Design Notes](#known-design-notes)

## Architecture

The application is split into three services:

| Service | Local port | Responsibility |
|---|---:|---|
| User Service | `8081` | Registration, login, and user profile management |
| Post Service | `8080` | Create, retrieve, update, and delete posts |
| Feed Service | `8082` | Retrieve the feed and manage post likes |

The Feed Service calls the Post Service to retrieve posts and validate a post before recording a like. It forwards the caller's bearer token to protected downstream endpoints.

### High-level flow

```text
                    Client / UI
                        |
                   Bearer JWT
                        |
          +-------------+-------------+
          |             |             |
          v             v             v
     User Service  Post Service  Feed Service
       :8081         :8080         :8082
                         ^             |
                         +-------------+
                           REST calls

  PostgreSQL: user, post, and like persistence
  Google Cloud Storage: uploaded images
```

This is a logical view. Confirm the actual database instance/schema arrangement and final cloud networking from the deployment configuration.

## Technology Stack

- Java 21
- Spring Boot 4.1.x (confirm each service's exact version in its `pom.xml`)
- Maven
- Spring Data JPA / Hibernate
- PostgreSQL
- JWT bearer-token authentication
- Google Cloud Storage (GCS) for image files
- Google Kubernetes Engine (GKE) as the target deployment platform

## Services and APIs

The following endpoints reflect the current implementation. For complete request/response schemas, see the API Documentation workbook.

### User Service — `http://localhost:8081`

| Method | Endpoint | Purpose | Authentication |
|---|---|---|---|
| `POST` | `/api/v1/users` | Register a user | Public |
| `POST` | `/api/v1/auth/login` | Log in and obtain an access token | Public |
| `GET` | `/api/v1/users/me` | Retrieve the current user's profile | Required |
| `PUT` | `/api/v1/users/me` | Update profile fields and optional profile picture | Required |

Registration accepts `userName`, `password`, and `email`. Login accepts `userNameOrEmail` and `password`. Profile updates use `multipart/form-data` with optional `bio` and `profilepic` fields.

### Post Service — `http://localhost:8080`

| Method | Endpoint | Purpose | Authentication |
|---|---|---|---|
| `POST` | `/api/v1/posts` | Create a post | Required |
| `GET` | `/api/v1/posts/{postId}` | Retrieve a post by UUID | Required |
| `PUT` | `/api/v1/posts/{postId}` | Update a post | Required |
| `DELETE` | `/api/v1/posts/{postId}` | Delete a post | Required |
| `GET` | `/api/v1/posts/posts` | Retrieve all posts, newest first | Required |

Create and update requests use `multipart/form-data`, with a `caption` and optional `image`. Text-only posts are supported. When updating a post without supplying a new image, the existing image URL is preserved. Post update and deletion enforce ownership.

> The all-posts route currently uses the repeated `posts` segment (`/api/v1/posts/posts`). Keep clients consistent with this route unless the API is deliberately changed.

### Feed Service — `http://localhost:8082`

| Method | Endpoint | Purpose | Authentication |
|---|---|---|---|
| `GET` | `/api/v1/feed` | Retrieve the feed | Required |
| `POST` | `/api/v1/posts/{postId}/like` | Like a post | Required |

The feed currently returns posts from the Post Service, newest first. The like operation rejects attempts to like one's own post with HTTP `400` and duplicate likes with HTTP `409`.

## Prerequisites

Install or arrange the following before running the application:

- JDK 21
- Maven (or use the Maven wrapper if one is included in the repository)
- PostgreSQL database access
- Google Cloud access and credentials if testing image uploads
- IntelliJ IDEA or another Java IDE (optional)

The exact database names, database users, environment variable names, and GCS permissions depend on the configuration in each service. Do not assume the example placeholders below are actual project values.

## Repository Structure

The repository contains three Spring Boot services. The exact top-level directory names may vary slightly depending on the current checkout.

```text
sharepic/
├── user-service/
├── post-service/
├── feed-service/
├── README.md
└── ...
```

Each service is built and run independently.

## Configuration

Configure each service using its local `application.properties` or other configuration mechanism. Keep local and cloud configuration separate.

Typical configuration categories include:

- PostgreSQL JDBC URL, username, and password
- JWT signing/validation secret configuration
- Google Cloud Storage bucket and credentials/access configuration
- Feed Service's Post Service base URL
- Server port

Use environment variables or an approved secret-management solution for credentials. Example placeholder names (adapt these to the property names actually used by the code):

```bash
# Examples only — replace with the configuration keys your service reads.
DB_URL=<your-jdbc-url>
DB_USERNAME=<your-database-username>
DB_PASSWORD=<your-database-password>
JWT_SECRET=<your-base64-encoded-jwt-secret>
GCS_BUCKET=<your-gcs-bucket>
POST_SERVICE_BASE_URL=http://localhost:8080
```

Do not commit real passwords, JWT secrets, service-account key files, or access tokens. Ensure that the JWT configuration used to sign tokens is compatible with the configuration used by services that validate them.

## Run Locally

Start PostgreSQL and configure the database settings for the services before starting the applications.

From the repository root, open a terminal in each service directory and run the service separately:

```bash
cd user-service
mvn spring-boot:run
```

```bash
cd post-service
mvn spring-boot:run
```

```bash
cd feed-service
mvn spring-boot:run
```

Start the Post Service before exercising Feed Service endpoints that call it. Ensure the Feed Service's configured Post Service URL points to `http://localhost:8080` for local testing.

If a service fails to start, check its application configuration, database connectivity, required cloud credentials, and startup logs.

## Authentication

1. Register through `POST /api/v1/users`.
2. Log in through `POST /api/v1/auth/login`.
3. Copy the returned `accessToken` from the login response.
4. Send the token to protected endpoints in the HTTP header:

```http
Authorization: Bearer <access-token>
```

The token is used by the services to identify the authenticated user. Do not paste live tokens into source control, documentation, tickets, or chat.

## Image Storage

Image uploads use Google Cloud Storage. The database stores the associated image URL rather than the image binary itself. Post image objects use the `post-images/` prefix in the current implementation.

An image URL is not necessarily publicly accessible; access depends on the bucket's IAM and URL-access configuration. Verify bucket permissions and the intended access pattern before cloud deployment.

## Testing

Run the Maven tests from each service directory:

```bash
mvn test
```

Run the tests for all three services separately. For end-to-end testing, verify at least:

- Registration and login
- Invalid credentials and requests without a valid token
- Current profile retrieval and profile update
- Creating a text-only post and an image post
- Retrieving, updating, and deleting a post
- Feed retrieval
- Successful likes, duplicate-like rejection, and own-post-like rejection
- Database and GCS connectivity in the target environment

Record actual test results separately; this README does not claim that every scenario has been run in every environment.

## Cloud Deployment

### Verified GCP environment

| Component | Configuration |
|---|---|
| GCP project | `project-7724e29a-0202-4802-903` |
| Region | `us-east1` |
| GKE cluster | `sharepic-cluster` (Autopilot) |
| Artifact Registry | `us-east1-docker.pkg.dev/project-7724e29a-0202-4802-903/sharepic-repo` |
| Cloud SQL | `sharepic-1` (PostgreSQL 16.15) |
| Kubernetes service account | `sharepic-ksa` |
| Runtime Kubernetes Secret | `sharepic-runtime-secrets` |
| GCS bucket | `sharepic-profile-pictures-1` |
| Current release | `20261028162646` |
| Application URL | `http://136.82.121.18` |

The public Ingress was created manually in the GCP Console and routes HTTP traffic on port 80 to the `sharepic-ui` Service. HTTPS/TLS is not configured or verified.

### Runtime architecture

- Angular is served by Nginx in the `sharepic-ui` workload.
- Nginx routes user and authentication APIs to `user-service:8081`, feed and like APIs to `feed-service:8082`, and post APIs to `post-service:8080`.
- Backend workloads connect to Cloud SQL PostgreSQL through Cloud SQL Auth Proxy sidecars.
- Profile and post images are stored in Google Cloud Storage.
- Each of the four deployments currently has one ready replica. Application-level high availability and autoscaling have not been verified.

### Deployment scripts

`deploy.sh` builds four container images using Cloud Build and publishes them to Artifact Registry with a timestamp tag. By default, it builds images only. It uses the current local working tree, which may differ from GitHub.

Build images only:

    ./deploy.sh

Build images and explicitly request a live deployment:

    ./deploy.sh --deploy

Deploy an existing release:

    ./deploy-release.sh 20261028162646

The release script verifies that the four images exist, requests confirmation, updates each deployment, and waits for rollouts. A failure partway through can leave some services updated and others unchanged; this is not an atomic release.

### Validation and production readiness

The manifests passed client-side dry-run validation. This validates manifest parsing, not a full deployment. Release `20261028162646` exists in Artifact Registry and is running in GKE, with one ready replica per deployment. The public frontend returned HTTP 200; this is not a full end-to-end regression test.

Before production use, review HTTPS/TLS, backend health probes, replica counts and autoscaling, Cloud SQL HA/backups/restore, GCS IAM and recovery, secret access, JWT security, CORS, monitoring, and automated integration testing. The current password-login/JWT implementation should not be described as a complete OAuth2 flow unless separately verified.

## Documentation

Keep these project artefacts together:

- **API Documentation workbook** — endpoint methods, paths, request/response details, and error scenarios.
- **Entity diagram PDF** — visual data model for users, posts, and post likes.
- **Technical Design Specification (TDS)** — architecture, service responsibilities, data design, security, deployment approach, and open items.

## Security Notes

- Never commit secrets or credentials to Git.
- Rotate any credential that has accidentally been exposed.
- Use least-privilege database and GCS permissions.
- Do not log passwords, bearer tokens, or secret values.
- Confirm password hashing, token expiry, signing-secret rotation, and HTTPS/TLS settings before a production deployment.
- Use separate configuration for local development and cloud deployment.

## Known Design Notes

- The current all-posts endpoint is `GET /api/v1/posts/posts`.
- The Feed Service uses the Post Service for post retrieval/validation and forwards the bearer token.
- The like table uses a composite key of `post_id` and `user_id`.
- Exact deployment details, database topology, and image-access policy should be updated after verifying the final cloud configuration.

---

**Maintainer:** SharePic project team  
**Document status:** Working README — keep it aligned with the code as endpoints and deployment configuration evolve.
