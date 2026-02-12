/**
 * Displays transaction history for currently selected account.
 */
export default function TransactionList({ transactions, selectedAccountId }) {
  return (
    <div className="card">
      <h3>Transactions {selectedAccountId ? `for account #${selectedAccountId}` : ''}</h3>
      <ul>
        {transactions.map((transaction) => (
          <li key={transaction.id}>
            [{transaction.type}] ${transaction.amount.toFixed(2)} - {transaction.description}
          </li>
        ))}
      </ul>
    </div>
  );
}
