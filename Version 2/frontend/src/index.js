// --- DOM Elements ---
const sidebarCollapse = document.getElementById('sidebarCollapse');
const statusAlertContainer = document.getElementById('statusAlertContainer');
const transactionsTableBody = document.getElementById('transactionsTableBody');
const totalTransactionsSpan = document.getElementById('totalTransactions');
const refreshTransactionsButton = document.getElementById('refreshTransactions');

// Modals
const transactionTypeSelectionModalElement = document.getElementById('transactionTypeSelectionModal');
const transactionTypeSelectionModal = new bootstrap.Modal(transactionTypeSelectionModalElement);
const createTransactionModalElement = document.getElementById('createTransactionModal');
const createTransactionModal = new bootstrap.Modal(createTransactionModalElement);
const updateTransactionModalElement = document.getElementById('updateTransactionModal');
const updateTransactionModal = new bootstrap.Modal(updateTransactionModalElement);

// Forms and Inputs
const createTransactionForm = document.getElementById('createTransactionForm');
const updateTransactionForm = document.getElementById('updateTransactionForm');
const transactionTypeHidden = document.getElementById('transactionTypeHidden');
const transactionTypeDisplay = document.getElementById('transactionTypeDisplay');
const amountSignHint = document.getElementById('amountSignHint');

// Transaction Type Selection Buttons
const selectDebitBtn = document.getElementById('selectDebitBtn');
const selectPaymentBtn = document.getElementById('selectPaymentBtn');

// View Sections
const dashboardLink = document.getElementById('dashboardLink');
const viewTransactionsLink = document.getElementById('viewTransactionsLink');
const viewCustomerLink = document.getElementById('viewCustomerLink');
const dashboardOverviewSection = document.getElementById('dashboardOverviewSection');
const viewTransactionsSection = document.getElementById('viewTransactionsSection');
const viewCustomerSection = document.getElementById('viewCustomerSection');
const debitTransactionsTableBody = document.getElementById('debitTransactionsTableBody');
const paymentTransactionsTableBody = document.getElementById('paymentTransactionsTableBody');


// --- Helper Functions ---

/**
 * Displays a Bootstrap alert message.
 * @param {string} message The message to display.
 * @param {'success'|'danger'|'warning'|'info'} type The type of alert (e.g., 'success', 'danger').
 * @param {number} duration Optional. Duration in milliseconds before the alert auto-hides. Default is 5000ms.
 */
function displayAlert(message, type, duration = 5000) {
    statusAlertContainer.innerHTML = ''; // Clear any existing alerts
    const alertHtml = `
                <div class="alert alert-${type} alert-dismissible fade show" role="alert">
                    ${message}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            `;
    statusAlertContainer.innerHTML = alertHtml;

    if (duration > 0) {
        setTimeout(() => {
            const currentAlert = statusAlertContainer.querySelector('.alert');
            if (currentAlert) {
                const bsAlert = bootstrap.Alert.getInstance(currentAlert);
                if (bsAlert) {
                    bsAlert.dispose();
                } else {
                    currentAlert.remove();
                }
            }
        }, duration);
    }
}

/**
 * Shows a specific content section and hides others.
 * @param {string} sectionId The ID of the section to show (e.g., 'dashboardOverviewSection', 'viewTransactionsSection').
 */
function showSection(sectionId) {
    document.querySelectorAll('.content-section').forEach(section => {
        section.classList.remove('active');
    });
    document.getElementById(sectionId).classList.add('active');

    // Update sidebar active class
    document.querySelectorAll('.sidebar-items li').forEach(li => li.classList.remove('active'));
    if (sectionId === 'dashboardOverviewSection') {
        dashboardLink.parentElement.classList.add('active');
    } else if (sectionId === 'viewTransactionsSection') {
        viewTransactionsLink.parentElement.classList.add('active');
    }
}

/**
 * Renders transactions into the main table and also into debit/payment tables.
 * @param {Array<Object>} transactions The list of transaction objects.
 */
function renderTransactions(transactions) {
    transactionsTableBody.innerHTML = '';
    debitTransactionsTableBody.innerHTML = '';
    paymentTransactionsTableBody.innerHTML = '';

    if (transactions.length === 0) {
        transactionsTableBody.innerHTML = '<tr><td colspan="7" class="text-center">No transactions found.</td></tr>';
        debitTransactionsTableBody.innerHTML = '<tr><td colspan="5" class="text-center">No debit transactions found.</td></tr>';
        paymentTransactionsTableBody.innerHTML = '<tr><td colspan="5" class="text-center">No payment transactions found.</td></tr>';
        totalTransactionsSpan.textContent = 0;
        return;
    }

    let debitCount = 0;
    let paymentCount = 0;

    transactions.forEach(transaction => {
        // Render to main dashboard table
        const mainRow = transactionsTableBody.insertRow();
        mainRow.insertCell(0).textContent = transaction.transaction_id;
        mainRow.insertCell(1).textContent = transaction.name;
        const transactionDate = new Date(transaction.date);
        mainRow.insertCell(2).textContent = transactionDate.toLocaleString();
        mainRow.insertCell(3).textContent = transaction.vendor;
        mainRow.insertCell(4).textContent = transaction.description;
        mainRow.insertCell(5).textContent = `$${parseFloat(transaction.amount).toFixed(2)}`;

        const actionsCell = mainRow.insertCell(6);
        actionsCell.innerHTML = `
                    <button class="btn btn-warning btn-sm me-1 edit-btn" data-id="${transaction.transaction_id}"
                        data-name="${transaction.name}" 
                        data-date="${transaction.date}" 
                        data-vendor="${transaction.vendor}" 
                        data-description="${transaction.description}" 
                        data-amount="${transaction.amount}">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="btn btn-danger btn-sm delete-btn" data-id="${transaction.transaction_id}">
                        <i class="fas fa-trash-alt"></i>
                    </button>
                `;

        // Render to specific debit/payment tables
        const amount = parseFloat(transaction.amount);
        const targetTableBody = amount >= 0 ? debitTransactionsTableBody : paymentTransactionsTableBody;
        const specificRow = targetTableBody.insertRow();
        specificRow.insertCell(0).textContent = transaction.transaction_id;
        specificRow.insertCell(1).textContent = transaction.name;
        specificRow.insertCell(2).textContent = transactionDate.toLocaleString();
        specificRow.insertCell(3).textContent = transaction.vendor;
        specificRow.insertCell(4).textContent = `$${amount.toFixed(2)}`;

        if (amount >= 0) {
            debitCount++;
        } else {
            paymentCount++;
        }
    });

    totalTransactionsSpan.textContent = transactions.length;

    // Update no transactions message if tables are empty after filtering
    if (debitCount === 0) {
        debitTransactionsTableBody.innerHTML = '<tr><td colspan="5" class="text-center">No debit transactions found.</td></tr>';
    }
    if (paymentCount === 0) {
        paymentTransactionsTableBody.innerHTML = '<tr><td colspan="5" class="text-center">No payment transactions found.</td></tr>';
    }
}


// --- Fetch Data Functions ---

async function fetchTransactions() {
    try {
        const response = await fetch('http://localhost:8080/transactions');
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        const transactions = await response.json();
        renderTransactions(transactions); // Use the new render function
    } catch (error) {
        console.error('Error fetching transactions:', error);
        transactionsTableBody.innerHTML = `<tr><td colspan="7" class="text-center text-danger">Failed to load transactions: ${error.message}</td></tr>`;
        debitTransactionsTableBody.innerHTML = `<tr><td colspan="5" class="text-center text-danger">Failed to load debit transactions: ${error.message}</td></tr>`;
        paymentTransactionsTableBody.innerHTML = `<tr><td colspan="5" class="text-center text-danger">Failed to load payment transactions: ${error.message}</td></tr>`;
        totalTransactionsSpan.textContent = 'Error';
        displayAlert(`Failed to load transactions: ${error.message}`, 'danger');
    }
}


// --- Event Listeners ---

sidebarCollapse.addEventListener('click', function () {
    document.getElementById('sidebar').classList.toggle('active');
});

// Initial "Create Transaction" button click -> show type selection modal
document.querySelector('[data-bs-target="#transactionTypeSelectionModal"]').addEventListener('click', function () {
    createTransactionForm.reset(); // Clear form before selection
    // No need to explicitly show transactionTypeSelectionModal, data-bs-toggle handles it
});

// Select Debit Button click
selectDebitBtn.addEventListener('click', function () {
    transactionTypeHidden.value = 'debit';
    transactionTypeDisplay.textContent = 'Debit';
    amountSignHint.textContent = '(Positive value expected)';
    transactionTypeSelectionModal.hide();
    createTransactionModal.show();
});

// Select Payment Button click
selectPaymentBtn.addEventListener('click', function () {
    transactionTypeHidden.value = 'payment';
    transactionTypeDisplay.textContent = 'Payment';
    amountSignHint.textContent = '(Negative value expected)';
    transactionTypeSelectionModal.hide();
    createTransactionModal.show();
});


// Create Transaction Form Submission
createTransactionForm.addEventListener('submit', async function (event) {
    event.preventDefault();

    const customerName = document.getElementById('customerName').value;
    const transactionDate = document.getElementById('transactionDate').value;
    const vendor = document.getElementById('vendor').value;
    const description = document.getElementById('description').value;
    let amount = parseFloat(document.getElementById('amount').value); // Use 'let' to modify

    const type = transactionTypeHidden.value; // Get the selected type

    // Adjust amount based on type
    if (type === 'payment' && amount > 0) {
        amount = -amount; // Ensure payment is negative
    } else if (type === 'debit' && amount < 0) {
        amount = Math.abs(amount); // Ensure debit is positive
    }

    const newTransaction = {
        name: customerName,
        date: transactionDate,
        vendor: vendor,
        description: description,
        amount: amount
    };

    try {
        const response = await fetch('http://localhost:8080/transactions', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(newTransaction)
        });

        if (!response.ok) {
            const errorResponse = await response.json();
            let errorMessage = errorResponse.message || `HTTP error! Status: ${response.status}`;
            throw new Error(errorMessage);
        }

        const createdTransaction = await response.json();
        displayAlert(`Transaction created successfully! ID: ${createdTransaction.transaction_id}`, 'success');
        createTransactionModal.hide();
        createTransactionForm.reset();
        fetchTransactions(); // Refresh all tables
    } catch (error) {
        console.error('Error creating transaction:', error);
        displayAlert(`Failed to create transaction: ${error.message}`, 'danger');
    }
});

// Event delegation for Update and Delete buttons
transactionsTableBody.addEventListener('click', async function (event) {
    // Delete Button Click
    if (event.target.closest('.delete-btn')) {
        const button = event.target.closest('.delete-btn');
        const transactionId = button.dataset.id;

        if (confirm(`Are you sure you want to delete transaction ID ${transactionId}?`)) {
            try {
                const response = await fetch(`http://localhost:8080/transactions/${transactionId}`, {
                    method: 'DELETE'
                });

                if (!response.ok) {
                    const errorText = await response.text();
                    throw new Error(`HTTP error! Status: ${response.status} - ${errorText}`);
                }

                displayAlert(`Transaction ID ${transactionId} deleted successfully!`, 'success');
                fetchTransactions();
            } catch (error) {
                console.error('Error deleting transaction:', error);
                displayAlert(`Failed to delete transaction ID ${transactionId}: ${error.message}`, 'danger');
            }
        }
    }

    // Edit Button Click
    if (event.target.closest('.edit-btn')) {
        const button = event.target.closest('.edit-btn');
        const transactionId = button.dataset.id;

        document.getElementById('updateTransactionId').value = transactionId;
        document.getElementById('updateCustomerName').value = button.dataset.name;

        let dateValue = button.dataset.date;
        if (dateValue) {
            const parts = dateValue.split(':');
            if (parts.length > 2) {
                dateValue = parts[0] + ':' + parts[1];
            }
            document.getElementById('updateTransactionDate').value = dateValue;
        }

        document.getElementById('updateVendor').value = button.dataset.vendor;
        document.getElementById('updateDescription').value = button.dataset.description;
        document.getElementById('updateAmount').value = parseFloat(button.dataset.amount || 0).toFixed(2);

        updateTransactionModal.show();
    }
});

// Event listener for Update Form Submission
updateTransactionForm.addEventListener('submit', async function (event) {
    event.preventDefault();

    const transactionId = document.getElementById('updateTransactionId').value;
    const updatedTransaction = {
        transaction_id: parseInt(transactionId),
        name: document.getElementById('updateCustomerName').value,
        date: document.getElementById('updateTransactionDate').value,
        vendor: document.getElementById('updateVendor').value,
        description: document.getElementById('updateDescription').value,
        amount: parseFloat(document.getElementById('updateAmount').value)
    };

    try {
        const response = await fetch(`http://localhost:8080/transactions/${transactionId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updatedTransaction)
        });

        if (!response.ok) {
            // Still try to get error message if response is not ok and has a body
            const errorText = await response.text(); // Get raw text to avoid JSON parsing errors
            let errorMessage = errorText || `HTTP error! Status: ${response.status}`;
            try {
                // Attempt to parse as JSON if it looks like one, for more detailed errors
                const errorJson = JSON.parse(errorText);
                errorMessage = errorJson.message || errorMessage;
            } catch (parseError) {
                // Not JSON, use plain text
            }
            throw new Error(errorMessage);
        }

        displayAlert(`Transaction ID ${transactionId} updated successfully!`, 'success');
        updateTransactionModal.hide();
        fetchTransactions();

    } catch (error) {
        console.error('Error updating transaction:', error);
        displayAlert(`Failed to update transaction ID ${transactionId}: ${error.message}`, 'danger');
    }
});


// --- Initial Load & Navigation ---

// Fetch transactions and show dashboard on initial load
document.addEventListener('DOMContentLoaded', function () {
    showSection('dashboardOverviewSection'); // Show dashboard by default
    fetchTransactions(); // Fetch transactions for the dashboard table
});

// Sidebar Navigation
dashboardLink.addEventListener('click', function (event) {
    event.preventDefault();
    showSection('dashboardOverviewSection');
    fetchTransactions(); // Refresh transactions for dashboard table
});

viewTransactionsLink.addEventListener('click', function (event) {
    event.preventDefault();
    showSection('viewTransactionsSection');
    fetchTransactions(); // Fetch and render for both debit/payment tables
});

// Refresh button on dashboard
refreshTransactionsButton.addEventListener('click', fetchTransactions);