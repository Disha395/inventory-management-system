import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import ApiService from "../service/ApiService.js";
import Layout from "../component/Layout.jsx";

const TransactionDetailsPage = () => {
    const { transactionId } = useParams();

    const [transaction, setTransaction] = useState(null);
    const [message, setMessage] = useState("");
    const [status, setStatus] = useState("");

    const navigate = useNavigate();

    useEffect(() => {
        const getTransaction = async () => {
            try {
                const response = await ApiService.getTransactionById(transactionId);

                if (response.status === 200) {
                    setTransaction(response.transaction);
                    setStatus(response.transaction.status);
                }
            } catch (error) {
                showMessage(
                    error.response?.data?.message ||
                    "Error fetching transaction details"
                );
            }
        };

        getTransaction();
    }, [transactionId]);

    // Update transaction status
    const handleStatusChange = async () => {
        try {
            await ApiService.updateTransactionStatus(transactionId, status);
            navigate("/transaction");
        } catch (error) {
            showMessage(
                error.response?.data?.message ||
                "Error updating transaction status"
            );
        }
    };

    const showMessage = (msg) => {
        setMessage(msg);
        setTimeout(() => setMessage(""), 4000);
    };

    return (
        <Layout>
            {message && <p className="message">{message}</p>}

            <div className="transaction-details-page">
                {transaction && (
                    <>
                        {/* Transaction Info */}
                        <div className="section-card">
                            <h2>Transaction Information</h2>
                            <p>Type: {transaction.transactionType}</p>
                            <p>Status: {transaction.status}</p>
                            <p>Description: {transaction.description}</p>
                            <p>Note: {transaction.note}</p>
                            <p>Total Products: {transaction.totalProducts}</p>
                            <p>Total Price: {transaction.totalPrice?.toFixed(2)}</p>
                            <p>
                                Created At:{" "}
                                {new Date(transaction.createdAt).toLocaleString()}
                            </p>

                            {transaction.updatedAt && (
                                <p>
                                    Updated At:{" "}
                                    {new Date(transaction.updatedAt).toLocaleString()}
                                </p>
                            )}
                        </div>

                        {/* Product Info */}
                        <div className="section-card">
                            <h2>Product Information</h2>
                            <p>Name: {transaction.product?.name}</p>
                            <p>SKU: {transaction.product?.sku}</p>
                            <p>
                                Price: {transaction.product?.price?.toFixed(2)}
                            </p>
                            <p>
                                Stock Quantity:{" "}
                                {transaction.product?.stockQuantity}
                            </p>
                            <p>Description: {transaction.product?.description}</p>

                            {transaction.product?.imageUrl && (
                                <img
                                    src={transaction.product.imageUrl}
                                    alt={transaction.product.name}
                                />
                            )}
                        </div>

                        {/* User Info */}
                        <div className="section-card">
                            <h2>User Information</h2>
                            <p>Name: {transaction.user?.name}</p>
                            <p>Email: {transaction.user?.email}</p>
                            <p>Phone: {transaction.user?.phoneNumber}</p>
                            <p>Role: {transaction.user?.role}</p>
                            <p>
                                Created At:{" "}
                                {new Date(transaction.createdAt).toLocaleString()}
                            </p>
                        </div>

                        {/* Supplier Info */}
                        {transaction.supplier && (
                            <div className="section-card">
                                <h2>Supplier Information</h2>
                                <p>Name: {transaction.supplier.name}</p>
                                <p>Contact Info: {transaction.supplier.contactInfo}</p>
                                <p>Address: {transaction.supplier.address}</p>
                            </div>
                        )}

                        {/* Status Update */}
                        <div className="section-card transaction-status-update">
                            <label>Status: </label>

                            <select
                                value={status}
                                onChange={(e) => setStatus(e.target.value)}
                            >
                                <option value="PENDING">PENDING</option>
                                <option value="PROCESSING">PROCESSING</option>
                                <option value="COMPLETED">COMPLETED</option>
                                <option value="CANCELLED">CANCELLED</option>
                            </select>

                            <button onClick={handleStatusChange}>
                                Update Status
                            </button>
                        </div>
                    </>
                )}
            </div>
        </Layout>
    );
};

export default TransactionDetailsPage;