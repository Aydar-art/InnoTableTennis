# TableTennis

Table Tennis site of Innopolis University

tabletennis.innopolis.university

## Build postgres image with prod data

```shell
docker image build -t system205/tabletennis-postgres:28.04.24 --rm -f Dockerfile.postgres.dev .
```
