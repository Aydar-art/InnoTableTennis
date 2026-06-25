# ScoreScout

## Developing

1. Pull the repository
2. Install dependencies with `pnpm i`
3. Start a development server:

```bash
pnpm run dev
```

## Building

To build the project, run:

```bash
docker build -t scorescout .
```

To run the project, run:

```bash
docker run -p 3000:3000 scorescout
```
