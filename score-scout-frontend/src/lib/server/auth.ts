import jwt from 'jsonwebtoken';

const JWT_SECRET = 'SECRET_KEY';

export function createToken(email: string): string {
  return jwt.sign(
    {
      email: email,
    },
    JWT_SECRET,
    {
      expiresIn: '1d',
    }
  );
}

export function verifyToken(token: string): string | null {
  try {
    const payload = jwt.verify(token, JWT_SECRET) as { email: string };
    return payload.email;
  } catch {
    return null;
  }
}
