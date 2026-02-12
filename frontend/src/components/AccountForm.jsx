import { useState } from 'react';

/**
 * Form component used to create new bank account.
 * Parent App component provides the onCreate callback.
 */
export default function AccountForm({ onCreate }) {
  const [form, setForm] = useState({ customerName: '', accountType: 'SAVINGS', openingBalance: 1000 });

  /**
   * Updates local form state whenever user edits a field.
   */
  const updateField = (event) => {
    setForm((current) => ({ ...current, [event.target.name]: event.target.value }));
  };

  /**
   * Submits form data to parent callback.
   */
  const submit = async (event) => {
    event.preventDefault();
    await onCreate({ ...form, openingBalance: Number(form.openingBalance) });
    setForm({ customerName: '', accountType: 'SAVINGS', openingBalance: 1000 });
  };

  return (
    <form className="card" onSubmit={submit}>
      <h3>Create Account</h3>
      <input name="customerName" placeholder="Customer Name" value={form.customerName} onChange={updateField} required />
      <select name="accountType" value={form.accountType} onChange={updateField}>
        <option value="SAVINGS">Savings</option>
        <option value="CURRENT">Current</option>
      </select>
      <input name="openingBalance" type="number" min="1" value={form.openingBalance} onChange={updateField} required />
      <button type="submit">Create</button>
    </form>
  );
}
