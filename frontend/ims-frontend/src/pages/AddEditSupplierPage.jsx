import React, { useState, useEffect } from "react";
import Layout from "../component/Layout";
import ApiService from "../service/ApiService";
import { useNavigate, useParams } from "react-router-dom";

const AddEditSupplierPage = () => {

    const { supplierId } = useParams();

    const [name, setName] = useState("");
    const [contactInfo, setContactInfo] = useState("");
    const [address, setAddress] = useState("");
    const [message, setMessage] = useState("");
    const [isEditing, setIsEditing] = useState(false);
    const [loading, setLoading] = useState(false);

    const navigate = useNavigate();

    useEffect(() => {

        if (supplierId) {

            setIsEditing(true);
            setLoading(true);

            const fetchSupplier = async () => {
                try {

                    const supplierData = await ApiService.getSupplierById(supplierId);

                    if (supplierData.status === 200) {
                        setName(supplierData.supplier.name);
                        setContactInfo(supplierData.supplier.contactInfo);
                        setAddress(supplierData.supplier.address);
                    }

                } catch (error) {

                    showMessage(
                        error.response?.data?.message || "Error fetching supplier"
                    );

                } finally {
                    setLoading(false);
                }
            };

            fetchSupplier();
        }

    }, [supplierId]);

    const handleSubmit = async (e) => {

        e.preventDefault();

        const supplierData = { name, contactInfo, address };

        try {

            if (isEditing) {

                await ApiService.updateSupplier(supplierId, supplierData);

                navigate("/supplier");

            } else {

                await ApiService.addSupplier(supplierData);

                navigate("/supplier");
            }

        } catch (error) {

            showMessage(
                error.response?.data?.message || "Error saving supplier"
            );
        }
    };

    const showMessage = (msg) => {
        setMessage(msg);
        setTimeout(() => setMessage(""), 4000);
    };

    return (
        <Layout>

            {message && <div className="message">{message}</div>}

            <div className="supplier-form-page">

                <h1>{isEditing ? "Edit Supplier" : "Add Supplier"}</h1>

                {loading ? (

                    <p>Loading...</p>

                ) : (

                    <form onSubmit={handleSubmit}>

                        <div className="form-group">
                            <label>Supplier Name</label>
                            <input
                                value={name}
                                onChange={(e) => setName(e.target.value)}
                                required
                                type="text"
                            />
                        </div>

                        <div className="form-group">
                            <label>Contact Info</label>
                            <input
                                value={contactInfo}
                                onChange={(e) => setContactInfo(e.target.value)}
                                required
                                type="text"
                            />
                        </div>

                        <div className="form-group">
                            <label>Address</label>
                            <input
                                value={address}
                                onChange={(e) => setAddress(e.target.value)}
                                required
                                type="text"
                            />
                        </div>

                        <button type="submit">
                            {isEditing ? "Update Supplier" : "Add Supplier"}
                        </button>

                    </form>

                )}

            </div>

        </Layout>
    );
};

export default AddEditSupplierPage;