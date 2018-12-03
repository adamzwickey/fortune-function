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

pfs function create save-fortune \
  --git-repo https://github.com/azwickey-pivotal/fortune-function.git \
  --image gcr.io/$GCP_PROJECT/save-fortune \
  --handler saveFortune --verbose \
--env-from SPRING_REDIS_HOST=secretKeyRef:r-secret:host --env-from SPRING_REDIS_PORT=secretKeyRef:r-secret:port --env-from SPRING_REDIS_PASSWORD=secretKeyRef:r-secret:password
```
. Test Functions
```bash
curl save-fortune.default.pfs.public.cloud.zwickey.net -H 'Content-Type: text/plain' -d "Things are looking up"
{"id":"2f1f4cbe-64af-429f-8f52-1a503b71ade4","text":"Things are looking up"}

curl fortune.default.pfs.public.cloud.zwickey.net -H 'Content-Type: text/plain' -d random
{"id":"2f1f4cbe-64af-429f-8f52-1a503b71ade4","text":"Things are looking up"}
```
