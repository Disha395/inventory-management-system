import React, { useEffect, useState } from 'react';
import { useNavigate } from "react-router-dom";
import ApiService from "../service/ApiService.js";
import Layout from "../component/Layout.jsx";
import PaginationComponent from "../component/PaginationComponent.jsx";

const TransactionsPage = () => {

    const [transactions, setTransactions] = useState([]);
    const [message, setMessage] = useState("");
    const [filter, setFilter] = useState("");
    const [valueToSearch, setValueToSearch] = useState("");

    const navigate = useNavigate();

    // Pagination
    const [currentPage, setCurrentPage] = useState(1);
    const [totalPages, setTotalPages] = useState(0);
    const itemsPerPage = 10;

    useEffect(() => {

        const getTransactions = async () => {
            try {

                const transactionData = await ApiService.getAllTransactions(valueToSearch);
                const allTransactions = transactionData?.transactions || [];

                setTotalPages(Math.ceil(allTransactions.length / itemsPerPage));

                setTransactions(
                    allTransactions.slice(
                        (currentPage - 1) * itemsPerPage,
                        currentPage * itemsPerPage
                    )
                );

            } catch (e) {

                showMessage(
                    e.response?.data?.message || "Error fetching transactions"
                );
            }
        };

        getTransactions();

    }, [currentPage, valueToSearch]);

    const showMessage = (msg) => {
        setMessage(msg);
        setTimeout(() => setMessage(""), 4000);
    };

    const handleSearch = () => {
        setCurrentPage(1);
        setValueToSearch(filter);
    };

    const navigateToTransactionDetails = (transactionId) => {
        navigate(`/transaction/${transactionId}`);
    };

    return (
        <Layout>

            {message && <p className="message">{message}</p>}

            <div className="transactions-page">

                <div className="transactions-header">
                    <h1>Transactions</h1>

                    <div className="transaction-search">
                        <input
                            placeholder="Search transaction..."
                            value={filter}
                            onChange={(e) => setFilter(e.target.value)}
                            type="text"
                        />
                        <button onClick={handleSearch}>Search</button>
                    </div>
                </div>

                {transactions.length > 0 ? (
                    <table className="transactions-table">

                        <thead>
                        <tr>
                            <th>TYPE</th>
                            <th>STATUS</th>
                            <th>TOTAL PRICE</th>
                            <th>TOTAL PRODUCTS</th>
                            <th>DATE</th>
                            <th>ACTIONS</th>
                        </tr>
                        </thead>

                        <tbody>
                        {transactions.map((transaction) => (
                            <tr key={transaction.id}>

                                <td>{transaction.transactionType}</td>
                                <td>{transaction.status}</td>
                                <td>{transaction.totalPrice}</td>
                                <td>{transaction.totalProducts}</td>
                                <td>
                                    {transaction.createdAt
                                        ? new Date(transaction.createdAt).toLocaleString()
                                        : "-"
                                    }
                                </td>

                                <td>
                                    <button
                                        onClick={() =>
                                            navigateToTransactionDetails(transaction.id)
                                        }
                                    >
                                        View Details
                                    </button>
                                </td>

                            </tr>
                        ))}
                        </tbody>

                    </table>
                ) : (
                    <p>No transactions found</p>
                )}

            </div>

            <PaginationComponent
                currentPage={currentPage}
                totalPages={totalPages}
                onPageChange={setCurrentPage}
            />

        </Layout>
    );
};

export default TransactionsPage;