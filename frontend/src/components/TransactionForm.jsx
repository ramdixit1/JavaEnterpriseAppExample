import { useState } from 'react';

/**
 * Form component that captures debit/credit transaction input.
 */
export default function TransactionForm({ accounts, onCreateTransaction }) {
  const [form, setForm] = useState({ accountId: '', type: 'DEBIT', amount: 1, description: '' });

  /**
   * Updates transaction form model on every input change.
   */
  const updateField = (event) => {
    setForm((current) => ({ ...current, [event.target.name]: event.target.value }));
  };

  /**
   * Sends transaction payload to parent App callback.
   */
  const submit = async (event) => {
    event.preventDefault();
    await onCreateTransaction({ ...form, accountId: Number(form.accountId), amount: Number(form.amount) });
    setForm({ accountId: '', type: 'DEBIT', amount: 1, description: '' });
  };

  return (
    <form className="card" onSubmit={submit}>
      <h3>Create Transaction</h3>
      <select name="accountId" value={form.accountId} onChange={updateField} required>
        <option value="">Select Account</option>
        {accounts.map((account) => (
          <option key={account.id} value={account.id}>{account.customerName} #{account.id}</option>
        ))}
      </select>
      <select name="type" value={form.type} onChange={updateField}>
        <option value="DEBIT">Debit</option>
        <option value="CREDIT">Credit</option>
      </select>
      <input name="amount" type="number" min="1" value={form.amount} onChange={updateField} required />
      <input name="description" placeholder="Description" value={form.description} onChange={updateField} required />
      <button type="submit">Post</button>
    </form>
  );
}
