# fortune-function

## Deploy/Install
. Install PFS
. Install k8s Service Catalog
. Enable Redis SB
. Create and Bind on resulting in a secret in r-secret
. Create Functions
```bash
export GCP_PROJECT=$(gcloud config get-value core/project)

pfs function create fortune \
  --git-repo https://github.com/azwickey-pivotal/fortune-function.git \
  --image gcr.io/$GCP_PROJECT/fortune \
  --handler getFortune --verbose \
--env-from SPRING_REDIS_HOST=secretKeyRef:r-secret:host --env-from SPRING_REDIS_PORT=secretKeyRef:r-secret:port --env-from SPRING_REDIS_PASSWORD=secretKeyRef:r-secret:password
```
. Test Functions
```bash
s = "Python syntax highlighting"
print s
```
