# GCP Implementation Checklist — 20261028162646

## Verified complete
- [x] Four images built with Cloud Build and pushed to Artifact Registry under `20261028162646`.
- [x] `deploy-release.sh` updated all four GKE deployments.
- [x] All rollouts completed; one ready replica per deployment.
- [x] Public frontend returned HTTP 200.

## Before demo
- [ ] Login, feed, images, create post, like another user's post, logout.
- [ ] Test text-only post behavior.
- [ ] Explain profile-edit UI status accurately.
- [ ] Do not display credentials, tokens or secrets.

## Not verified / hardening
- [ ] HTTPS/TLS.
- [ ] Multiple replicas, HPA, PDB and load/failure tests.
- [ ] Cloud SQL HA, backups, PITR and restore test.
- [ ] GCS least privilege, retention/versioning and recovery.
- [ ] Password hashing, JWT expiry/rotation, CORS, secret access review.
- [ ] Monitoring, uptime checks and alert policies.
- [ ] Automated integration tests and GitHub-triggered CI/CD.
- [ ] Full OAuth2 flow if explicitly required.
