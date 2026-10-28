#!/usr/bin/env bash
set -Eeuo pipefail

PROJECT="project-7724e29a-0202-4802-903"
REGION="us-east1"
REPOSITORY="sharepic-repo"
CLUSTER="sharepic-cluster"
CLUSTER_LOCATION="us-east1"

DEPLOY=false
if [[ "${1:-}" == "--deploy" ]]; then
  DEPLOY=true
elif [[ $# -gt 0 ]]; then
  echo "Usage: ./deploy.sh [--deploy]"
  exit 2
fi

cd "$(dirname "$0")"

if [[ "$(gcloud config get-value project 2>/dev/null)" != "$PROJECT" ]]; then
  echo "ERROR: Active gcloud project must be $PROJECT"
  exit 1
fi

for dir in user-service post-service feed-service sharepic-ui; do
  if [[ ! -f "$dir/Dockerfile" ]]; then
    echo "ERROR: Missing $dir/Dockerfile"
    exit 1
  fi
done

if [[ -n "$(git status --porcelain)" ]]; then
  echo "WARNING: Git has uncommitted or untracked changes."
  echo "The build will use the current local files, not necessarily GitHub main."
  git status --short
fi

RELEASE_TAG="$(date -u +%Y%m%d%H%M%S)"
IMAGE_BASE="${REGION}-docker.pkg.dev/${PROJECT}/${REPOSITORY}"

echo
echo "Release tag: $RELEASE_TAG"
echo "Project:     $PROJECT"
echo "Region:      $REGION"
echo "Deploy now:  $DEPLOY"
echo

if [[ "$DEPLOY" == true ]]; then
  read -r -p "This will update the live GKE application. Type DEPLOY to continue: " answer
  if [[ "$answer" != "DEPLOY" ]]; then
    echo "Cancelled."
    exit 0
  fi
fi

build_image() {
  local service="$1"
  echo
  echo "=== Building $service:$RELEASE_TAG ==="
  gcloud builds submit "$service" \
    --project="$PROJECT" \
    --region="$REGION" \
    --tag="${IMAGE_BASE}/${service}:${RELEASE_TAG}"
}

build_image user-service
build_image post-service
build_image feed-service
build_image sharepic-ui

echo
echo "All images built successfully."
echo "Release tag: $RELEASE_TAG"

if [[ "$DEPLOY" != true ]]; then
  echo
  echo "Build-only mode: live deployments were NOT changed."
  echo "After reviewing the images, deploy with:"
  echo "./deploy.sh --deploy"
  exit 0
fi

gcloud container clusters get-credentials "$CLUSTER" \
  --region="$CLUSTER_LOCATION" \
  --project="$PROJECT"

# Preserve the existing deployments and Cloud SQL sidecars.
kubectl set image deployment/user-service \
  user-service="${IMAGE_BASE}/user-service:${RELEASE_TAG}"
kubectl rollout status deployment/user-service --timeout=300s

kubectl set image deployment/post-service \
  post-service="${IMAGE_BASE}/post-service:${RELEASE_TAG}"
kubectl rollout status deployment/post-service --timeout=300s

kubectl set image deployment/feed-service \
  feed-service="${IMAGE_BASE}/feed-service:${RELEASE_TAG}"
kubectl rollout status deployment/feed-service --timeout=300s

kubectl set image deployment/sharepic-ui \
  sharepic-ui="${IMAGE_BASE}/sharepic-ui:${RELEASE_TAG}"
kubectl rollout status deployment/sharepic-ui --timeout=300s

echo
echo "Deployment rollouts completed."
kubectl get deployments user-service post-service feed-service sharepic-ui
echo "Release tag: $RELEASE_TAG"
