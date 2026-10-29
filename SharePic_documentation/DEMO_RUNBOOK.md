# SharePic Demo Runbook
**Demo:** 29 October 2026, 10:30 AM | **Release:** `20261028162646` | **App:** http://136.82.121.18

## Before the meeting
- Open app and test login using a prepared test account.
- Check feed, image rendering, post creation, like another user's post and logout.
- Keep GCP Console/Cloud Shell available; never show passwords, JWTs or secret values.
- Avoid last-minute infrastructure changes unless a test reveals a blocker.

## Suggested 10-minute flow
1. **Overview (1 min):** Angular social photo-sharing UI, three REST microservices.
2. **Architecture (2 min):** GKE Ingress → Nginx UI → User/Post/Feed; Cloud SQL stores structured data; GCS stores images.
3. **User journey (3 min):** Login → JWT → feed → create post/image → like another user's post → logout.
4. **Cloud proof (2 min):** GKE deployments, Cloud SQL, GCS, Artifact Registry release `20261028162646`.
5. **Deployment automation (1 min):** Cloud Build builds; Artifact Registry versions; deploy-release.sh updates GKE and waits for rollout.
6. **Known gaps (1 min):** HTTP-only ingress, one replica each, HPA not verified, manual deployment not full CI/CD, OAuth2 flow not claimed, profile-edit UI not confirmed.

## Architecture answer
“SharePic has an Angular frontend served by Nginx. Nginx routes API requests to three Spring Boot services: User Service handles registration/login/profile, Post Service manages posts and image references, and Feed Service retrieves the feed and handles likes. Workloads run on GKE. PostgreSQL on Cloud SQL stores relational data and GCS stores image objects. Feed Service calls Post Service over Kubernetes internal service DNS and forwards the bearer token for protected downstream calls.”

## Likely questions
**Why microservices?** Separate responsibilities and allow independent deployment; the trade-off is extra network and operational complexity. A monolith could also be reasonable at this scale.

**Why GKE instead of Cloud Run?** GKE demonstrates Kubernetes orchestration, internal service discovery and deployment control. Cloud Run could be simpler/cheaper for a small app; GKE is not automatically the cheapest choice.

**Why Cloud SQL and GCS?** Cloud SQL holds relational data; GCS holds image objects so large binaries aren't stored in database rows.

**Is this OAuth2?** “The current app uses password login and JWT bearer tokens. JWT is a token format; I am not claiming a complete OAuth2 authorization flow without verifying that flow.”

**Is it highly available?** “The current deployment has one ready replica per service. GKE is the runtime, but I would not claim application-level high availability yet. Replicas, disruption budgets, autoscaling and failure testing remain improvements; Cloud SQL HA and restore configuration also need verification.”

**How is deployment automated?** “Cloud Build builds four images, Artifact Registry stores release-tagged images, and a manually triggered script updates GKE and waits for rollouts. GitHub-triggered CI/CD and automated integration testing are future improvements.”

**How are secrets handled?** “Runtime configuration is supplied through a Kubernetes Secret; backend pods use a Kubernetes service account and Cloud SQL Auth Proxy. Production hardening includes least-privilege review, workload identity/secret access review, token rotation, HTTPS, CORS and GCS IAM.”

## Do not overclaim
- Rollouts completed and public frontend returned HTTP 200; not full end-to-end proof.
- Ingress is HTTP port 80, not verified HTTPS.
- One ready replica per deployment; HPA not verified.
- JWT login is not automatically OAuth2.
- Profile-edit UI is a known gap; backend endpoint exists.
- Confirm text-only post behavior before claiming the demo.
- Cloud SQL HA/backups, GCS retention and complete monitoring are not verified.

## If something fails live
Stay calm, describe what you observed, identify whether the boundary is UI/service/database/storage, explain the next diagnostic step, and don't claim a successful result without evidence.
