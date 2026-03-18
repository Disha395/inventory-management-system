import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import ApiService from "../service/ApiService";
import Layout from "../component/Layout";

const SupplierPage = () => {

    const [suppliers, setSuppliers] = useState([]);
    const [message, setMessage] = useState("");

    const navigate = useNavigate();

    useEffect(() => {

        const getSuppliers = async () => {
            try {

                const responseData = await ApiService.getAllSuppliers();

                if (responseData.status === 200) {
                    setSuppliers(responseData.suppliers);
                } else {
                    showMessage(responseData.message);
                }

            } catch (error) {
                showMessage(
                    error.response?.data?.message || "Error fetching suppliers"
                );
            }
        };

        getSuppliers();

    }, []);

    const showMessage = (msg) => {
        setMessage(msg);
        setTimeout(() => setMessage(""), 4000);
    };

    const handleDeleteSupplier = async (supplierId) => {

        if (window.confirm("Are you sure you want to delete this supplier?")) {
            try {

                await ApiService.deleteSupplier(supplierId);

                setSuppliers(prev =>
                    prev.filter(supplier => supplier.id !== supplierId)
                );

                showMessage("Supplier deleted successfully");

            } catch (error) {
                showMessage(
                    error.response?.data?.message || "Error deleting supplier"
                );
            }
        }
    };

    return (
        <Layout>

            {message && <div className="message">{message}</div>}

            <div className="supplier-page">

                <div className="supplier-header">
                    <h1>Suppliers</h1>

                    <div className="add-sup">
                        <button onClick={() => navigate("/add-supplier")}>
                            Add Supplier
                        </button>
                    </div>
                </div>

                {suppliers.length > 0 ? (

                    <ul className="supplier-list">

                        {suppliers.map((supplier) => (
                            <li className="supplier-item" key={supplier.id}>

                                <span>{supplier.name}</span>

                                <div className="supplier-actions">
                                    <button onClick={() => navigate(`/edit-supplier/${supplier.id}`)}>
                                        Edit
                                    </button>

                                    <button onClick={() => handleDeleteSupplier(supplier.id)}>
                                        Delete
                                    </button>
                                </div>

                            </li>
                        ))}

                    </ul>

                ) : (
                    <p>No suppliers found</p>
                )}

            </div>

        </Layout>
    );
};

export default SupplierPage;