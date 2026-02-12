import { useEffect, useState } from 'react';
import AccountForm from './components/AccountForm.jsx';
import TransactionForm from './components/TransactionForm.jsx';
import AccountList from './components/AccountList.jsx';
import TransactionList from './components/TransactionList.jsx';
import { createAccount, createTransaction, getAccounts, getTransactionsByAccount } from './services/api.js';

/**
 * Top-level React container that wires forms, lists, and API calls together.
 */
export default function App() {
  const [accounts, setAccounts] = useState([]);
  const [transactions, setTransactions] = useState([]);
  const [selectedAccountId, setSelectedAccountId] = useState(null);
  const [error, setError] = useState('');

  /**
   * Loads account list when app starts.
   */
  useEffect(() => {
    refreshAccounts();
  }, []);

  /**
   * Fetches latest account list from backend.
   */
  const refreshAccounts = async () => {
    try {
      const response = await getAccounts();
      setAccounts(response.data);
      setError('');
    } catch (apiError) {
      setError(apiError?.response?.data?.error || 'Failed to load accounts');
    }
  };

  /**
   * Creates account then refreshes list.
   */
  const handleCreateAccount = async (payload) => {
    try {
      await createAccount(payload);
      await refreshAccounts();
    } catch (apiError) {
      setError(apiError?.response?.data?.error || 'Failed to create account');
    }
  };

  /**
   * Creates transaction then refreshes account + selected transaction list.
   */
  const handleCreateTransaction = async (payload) => {
    try {
      await createTransaction(payload);
      await refreshAccounts();
      await handleSelectAccount(payload.accountId);
    } catch (apiError) {
      setError(apiError?.response?.data?.error || 'Failed to create transaction');
    }
  };

  /**
   * Loads transaction list for a selected account.
   */
  const handleSelectAccount = async (accountId) => {
    try {
      setSelectedAccountId(accountId);
      const response = await getTransactionsByAccount(accountId);
      setTransactions(response.data);
      setError('');
    } catch (apiError) {
      setError(apiError?.response?.data?.error || 'Failed to load transactions');
    }
  };

  return (
    <main>
      <h1>On-Prem Banking Platform</h1>
      {error && <p className="error">{error}</p>}
      <section className="grid">
        <AccountForm onCreate={handleCreateAccount} />
        <TransactionForm accounts={accounts} onCreateTransaction={handleCreateTransaction} />
        <AccountList accounts={accounts} onSelect={handleSelectAccount} />
        <TransactionList transactions={transactions} selectedAccountId={selectedAccountId} />
      </section>
    </main>
  );
}
