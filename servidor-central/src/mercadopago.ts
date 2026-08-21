import { MercadoPagoConfig, Payment } from 'mercadopago';

const client = new MercadoPagoConfig({
  accessToken: process.env.MP_ACCESS_TOKEN || ''
});

export async function createPixPayment(totalAmount: number, description: string) {
  const myFee = totalAmount * 0.10; // 10% Inviolável
  const clientAmount = totalAmount - myFee;

  const payment = new Payment(client);

  // Exemplo básico de criação de cobrança
  const response = await payment.create({
    body: {
      transaction_amount: totalAmount,
      description: description,
      payment_method_id: 'pix',
      payer: {
        email: 'cliente@email.com'
      }
    }
  });

  return {
    paymentId: response.id,
    status: response.status,
    pixCopiaECola: response.point_of_interaction?.transaction_data?.qr_code,
    qrCodeBase64: response.point_of_interaction?.transaction_data?.qr_code_base64,
    myFee,
    clientAmount
  };
}