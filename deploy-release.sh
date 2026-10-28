#!/usr/bin/env bash
set -Eeuo pipefail

PROJECT="project-7724e29a-0202-4802-903"
REGION="us-east1"
CLUSTER="sharepic-cluster"
TAG="${1:-}"
IMAGE_BASE="${REGION}-docker.pkg.dev/${PROJECT}/sharepic-repo"

if [[ -z "$TAG" ]]; then
  echo "Usage: ./deploy-release.sh RELEASE_TAG"
  exit 2
fi

if [[ "$(gcloud config get-value project 2>/dev/null)" != "$PROJECT" ]]; then
  echo "ERROR: Active gcloud project is incorrect."
  exit 1
fi

echo "Release to deploy: $TAG"
echo "Images:"
for service in user-service post-service feed-service sharepic-ui; do
  image="${IMAGE_BASE}/${service}:${TAG}"
  echo "  $image"
  if ! gcloud artifacts docker images describe "$image" \
      --project="$PROJECT" >/dev/null 2>&1; then
    echo "ERROR: Image not found: $image"
    exit 1
  fi
done

echo
read -r -p "Type DEPLOY to update the live GKE workloads: " answer
if [[ "$answer" != "DEPLOY" ]]; then
  echo "Cancelled. Live workloads were not changed."
  exit 0
fi

gcloud container clusters get-credentials "$CLUSTER" \
  --region="$REGION" --project="$PROJECT"

kubectl set image deployment/user-service \
  user-service="${IMAGE_BASE}/user-service:${TAG}"
kubectl rollout status deployment/user-service --timeout=300s

kubectl set image deployment/post-service \
  post-service="${IMAGE_BASE}/post-service:${TAG}"
kubectl rollout status deployment/post-service --timeout=300s

kubectl set image deployment/feed-service \
  feed-service="${IMAGE_BASE}/feed-service:${TAG}"
kubectl rollout status deployment/feed-service --timeout=300s

kubectl set image deployment/sharepic-ui \
  sharepic-ui="${IMAGE_BASE}/sharepic-ui:${TAG}"
kubectl rollout status deployment/sharepic-ui --timeout=300s

echo
kubectl get deployments user-service post-service feed-service sharepic-ui
echo "Deployment commands completed for release $TAG."
