
# bigquery

## setup

```
DATASET=mydataset
bq mk --schema schema/city.json -t ${DATASET}.city
```

## migrate

```
BUCKET=hoge
DATASET=mydataset
gsutil cp data/city.json gs://${BUCKET}/city.json
bq load --source_format=NEWLINE_DELIMITED_JSON ${DATASET}.city gs://${BUCKET}/city.json
```
