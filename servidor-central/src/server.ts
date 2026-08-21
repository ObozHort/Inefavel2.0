import express from 'express';
import dotenv from 'dotenv';
import { generateKey, validateKey, revokeKey } from './keys';
import { createPixPayment } from './mercadopago';

dotenv.config();

const app = express();
app.use(express.json());

// 1. Rota de Validação da Licença (Chamada pelo Bot)
app.get('/api/bot/validar', (req, res) => {
  const key = req.query.key as string;
  if (key && validateKey(key)) {
    return res.status(200).json({ status: 'AUTHORIZED' });
  }
  return res.status(401).json({ status: 'EXPIRED_OR_INVALID' });
});

// 2. Rota de Cobrança PIX (Apenas a sua API calcula os 10%)
app.post('/api/pagamento/gerar-pix', async (req, res) => {
  const licenseKey = req.headers['x-license-key'] as string;

  if (!licenseKey || !validateKey(licenseKey)) {
    return res.status(403).json({ error: 'Licença expirada ou inválida.' });
  }

  try {
    const { totalAmount, description } = req.body;
    const paymentData = await createPixPayment(totalAmount, description);
    return res.json(paymentData);
  } catch (error) {
    return res.status(500).json({ error: 'Erro ao gerar cobrança PIX.' });
  }
});

// 3. Rota para o seu Futuro Site (Gera Key automaticamente após a compra)
app.post('/api/site/gerar-key', (req, res) => {
  const siteToken = req.headers['x-site-token'];
  if (siteToken !== process.env.SITE_SECRET_TOKEN) {
    return res.status(403).send('Acesso negado.');
  }

  const { clientName } = req.body;
  const newKey = generateKey(clientName);
  return res.json(newKey);
});

// 4. Rota Admin para Revogar Keys de inadimplentes
app.post('/api/admin/revogar', (req, res) => {
  const { key } = req.body;
  const success = revokeKey(key);
  return res.json({ success });
});

const PORT = process.env.PORT || 7070;
app.listen(PORT, () => console.log(`[Servidor Central] Rodando na porta ${PORT}`));