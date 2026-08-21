import { randomUUID } from 'crypto';

export interface LicenseData {
  key: string;
  clientName: string;
  createdAt: Date;
  expiresAt: Date;
  active: boolean;
}

// Simulando banco de dados (no futuro seu site usará esta mesma coleção/banco)
const keysDatabase = new Map<string, LicenseData>();

export function generateKey(clientName: string): LicenseData {
  const now = new Date();
  const expiresAt = new Date(now);
  expiresAt.setDate(expiresAt.getDate() + 30); // Define exatamente 30 dias de validade

  const license: LicenseData = {
    key: `KEY-${randomUUID().substring(0, 8).toUpperCase()}`,
    clientName,
    createdAt: now,
    expiresAt,
    active: true,
  };

  keysDatabase.set(license.key, license);
  return license;
}

export function validateKey(key: string): boolean {
  const license = keysDatabase.get(key);
  if (!license || !license.active) return false;

  // Checa se os 30 dias expiraram
  if (new Date() > license.expiresAt) {
    license.active = false;
    return false;
  }

  return true;
}

export function revokeKey(key: string): boolean {
  const license = keysDatabase.get(key);
  if (license) {
    license.active = false;
    return true;
  }
  return false;
}