import axios from 'axios';

// Shared axios client. Base URL stays relative so Vite proxy can forward calls.
const api = axios.create({
  headers: {
    'Content-Type': 'application/json'
  }
});

/**
 * Creates account via gateway -> account-service.
 */
export const createAccount = (payload) => api.post('/accounts', payload);

/**
 * Fetches all accounts via gateway -> account-service.
 */
export const getAccounts = () => api.get('/accounts');

/**
 * Creates transaction via gateway -> transaction-service.
 */
export const createTransaction = (payload) => api.post('/transactions', payload);

/**
 * Fetches transactions by account.
 */
export const getTransactionsByAccount = (accountId) => api.get(`/transactions/account/${accountId}`);
