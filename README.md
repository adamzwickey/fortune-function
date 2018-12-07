# fortune-function

## Deploy/Install
1. Install PFS
```bash
pfs system install -m <path-to-pfs-system-manifest>
pfs namespace init default -m <path-to-pfs-system-manifest> --gcr <GCR-JSON>
```
2. Install k8s Service Catalog
```bash
helm init
kubectl create clusterrolebinding tiller-cluster-admin \
    --clusterrole=cluster-admin \
    --serviceaccount=kube-system:default
helm repo add svc-cat https://svc-catalog-charts.storage.googleapis.com
helm install svc-cat/catalog \
    --name catalog --namespace catalog
```
3. Enable Redis SB w/ the following commands after updating the basic auth creds in 01-redis-secret.yml and the broker user in 02-redis-broker.yml according to your env
```bash
kubectl apply -f ./service-catalog/01-redis-secret.yml
kubectl apply -f ./service-catalog/02-redis-broker.yml
```
4. Create and Bind on resulting in a secret in r-secret
```bash
kubectl apply -f ./service-catalog/03-redis-service-entry.yml
kubectl apply -f ./service-catalog/04-redis-instance.yml
kubectl apply -f ./service-catalog/05-redis-binding.yml
```
5. Create Functions
```bash
export GCP_PROJECT=$(gcloud config get-value core/project)

pfs function create fortune \
  --git-repo https://github.com/azwickey-pivotal/fortune-function.git \
  --image gcr.io/$GCP_PROJECT/fortune \
  --handler getFortune --verbose \
--env-from SPRING_REDIS_HOST=secretKeyRef:r-secret:host --env-from SPRING_REDIS_PORT=secretKeyRef:r-secret:port --env-from SPRING_REDIS_PASSWORD=secretKeyRef:r-secret:password

pfs function create fortunes \
  --git-repo https://github.com/azwickey-pivotal/fortune-function.git \
  --image gcr.io/$GCP_PROJECT/fortunes \
  --handler allFortunes --verbose \
--env-from SPRING_REDIS_HOST=secretKeyRef:r-secret:host --env-from SPRING_REDIS_PORT=secretKeyRef:r-secret:port --env-from SPRING_REDIS_PASSWORD=secretKeyRef:r-secret:password

pfs function create save-fortune \
  --git-repo https://github.com/azwickey-pivotal/fortune-function.git \
  --image gcr.io/$GCP_PROJECT/save-fortune \
  --handler saveFortune --verbose \
--env-from SPRING_REDIS_HOST=secretKeyRef:r-secret:host --env-from SPRING_REDIS_PORT=secretKeyRef:r-secret:port --env-from SPRING_REDIS_PASSWORD=secretKeyRef:r-secret:password
```
6. Test Functions
```bash
curl save-fortune.default.pfs.public.cloud.zwickey.net -H 'Content-Type: text/plain' -d "Things are looking up"
{"id":"2f1f4cbe-64af-429f-8f52-1a503b71ade4","text":"Things are looking up"}

curl fortune.default.pfs.public.cloud.zwickey.net -H 'Content-Type: text/plain' -d random
{"id":"2f1f4cbe-64af-429f-8f52-1a503b71ade4","text":"Things are looking up"}

```
7. Push the web UI
```bash
cd ui
cf push ui -m 64M -b staticfile_buildpack
cf set-env ui BACKEND default.pfs.public.cloud.zwickey.net
cf restage ui
```
