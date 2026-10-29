# SharePicGCP

Angular UI + three Java/Spring Boot REST microservices for social photo sharing.

**Snapshot:** 28 October 2026. Release `20261028162646` was rolled out to all four GKE deployments; each reported one ready replica, and `curl` to http://136.82.121.18/ returned HTTP 200. This is not a full regression-test result.

## Deployment inventory
- App: http://136.82.121.18 (public HTTP :80; HTTPS/TLS not configured/verified)
- GCP project: `project-7724e29a-0202-4802-903`; region `us-east1`; GKE Autopilot cluster `sharepic-cluster`
- Artifact Registry: `us-east1-docker.pkg.dev/project-7724e29a-0202-4802-903/sharepic-repo`; current release `20261028162646`
- Cloud SQL instance: `project-7724e29a-0202-4802-903:us-east1:sharepic-1`
- GCS bucket: `sharepic-profile-pictures-1`

## Architecture
```text
Browser --HTTP:80--> GKE Ingress --> sharepic-ui Service --> Nginx
                                                  |-- /api/v1/users, /api/v1/auth/ --> user-service:8081
                                                  |-- /api/v1/feed, .../like -------> feed-service:8082
                                                  |-- /api/v1/posts/... ------------> post-service:8080

Backend services --> Cloud SQL PostgreSQL via Cloud SQL Auth Proxy sidecars
Post/profile images --> Google Cloud Storage
Feed Service --> Post Service over Kubernetes internal service DNS
```

| Service | Responsibility |
|---|---|
| User Service (8081) | Registration, login, current profile/update; issues JWT |
| Post Service (8080) | Create/read/update/delete posts and image references |
| Feed Service (8082) | Feed retrieval and likes |
| UI/Nginx (80) | Serves Angular build and routes API traffic |

## API catalogue
| Method | Path | Purpose |
|---|---|---|
| POST | `/api/v1/users` | Registration (public) |
| POST | `/api/v1/auth/login` | Login (public) |
| GET/PUT | `/api/v1/users/me` | Current profile and update (JWT) |
| POST | `/api/v1/posts` | Create post; caption and optional image |
| GET/PUT/DELETE | `/api/v1/posts/{postId}` | Read/update/delete post |
| GET | `/api/v1/posts/posts` | List posts newest first |
| GET | `/api/v1/feed` | Feed |
| POST | `/api/v1/posts/{postId}/like` | Like post |

The `/api/v1/posts/posts` route reflects current controller mappings; keep the UI and Feed Service aligned with it.

## Data model
- `users`: UUID id, user_name, email, password_hash, role, bio, profile_pic_url, timestamps.
- `posts`: UUID id, user_id, caption, nullable image_url, timestamps.
- `post_likes`: composite key `(post_id, user_id)`, created_at.
Logical relationships: one user authors many posts; likes connect users and posts. Confirm exact foreign-key constraints against the live schema.

## Security and accuracy notes
Current implementation uses password login and JWT bearer-token authentication. JWT is a token format; do not describe it as a complete OAuth2 flow unless that flow is separately verified. Self-like returns HTTP 400; duplicate like returns HTTP 409. The ingress is HTTP-only at present. Verify password hashing, token expiry, CORS, secrets, and GCS IAM before production use.

## Deployment automation
- `./deploy.sh` builds four images using Cloud Build and publishes a unique timestamp tag; default mode is build-only.
- `./deploy-release.sh 20261028162646` checks images, prompts for `DEPLOY`, updates the four GKE deployments and waits for rollouts.
- This is manually triggered deployment automation, not full CI/CD. GitHub triggers and automated integration tests are not yet verified.

## Known gaps / verify
- Profile-edit UI not confirmed complete (backend endpoint exists).
- Test text-only post behavior before claiming it in the demo.
- One replica per deployment; HPA not verified.
- HTTPS/TLS, Cloud SQL HA/backups/restore, GCS retention/versioning, complete monitoring, and full OAuth2 flow are not verified.
- Complete browser smoke test: login, feed, images, post, like another user's post, logout.
- Never expose credentials, bearer tokens or secret values.
