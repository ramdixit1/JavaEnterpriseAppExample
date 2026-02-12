/**
 * Read-only component that shows all accounts and balances.
 */
export default function AccountList({ accounts, onSelect }) {
  return (
    <div className="card">
      <h3>Accounts</h3>
      <ul>
        {accounts.map((account) => (
          <li key={account.id}>
            <button onClick={() => onSelect(account.id)}>
              #{account.id} - {account.customerName} ({account.accountType}) : ${account.balance.toFixed(2)}
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}
