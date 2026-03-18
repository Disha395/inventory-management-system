import React, { useEffect, useState } from "react";
import ApiService from "../service/ApiService";
import Layout from "../component/Layout";

const PurchasePage = () => {

    const [products, setProducts] = useState([]);
    const [suppliers, setSuppliers] = useState([]);
    const [productId, setProductId] = useState("");
    const [supplierId, setSupplierId] = useState("");
    const [description, setDescription] = useState("");
    const [note, setNote] = useState("");
    const [quantity, setQuantity] = useState("");
    const [message, setMessage] = useState("");

    useEffect(() => {

        const fetchProductAndSuppliers = async () => {
            try {

                const productData = await ApiService.getAllProducts();
                const supplierData = await ApiService.getAllSuppliers();

                setProducts(productData?.products || []);
                setSuppliers(supplierData?.suppliers || []);

            } catch (error) {
                showMessage(
                    error.response?.data?.message || "Error fetching data"
                );
            }
        };

        fetchProductAndSuppliers();

    }, []);

    const handleSubmit = async (e) => {

        e.preventDefault();

        if (!productId || !supplierId || !quantity) {
            showMessage("Please fill in all fields");
            return;
        }

        const body = {
            productId,
            quantity: Number(quantity),
            supplierId,
            description,
            note,
        };

        try {

            const response = await ApiService.purchaseProduct(body);

            showMessage(response.message);
            resetForm();

        } catch (error) {

            showMessage(
                error.response?.data?.message || "Error purchasing product"
            );
        }
    };

    const resetForm = () => {
        setProductId("");
        setSupplierId("");
        setDescription("");
        setNote("");
        setQuantity("");
    };

    const showMessage = (msg) => {
        setMessage(msg);
        setTimeout(() => setMessage(""), 4000);
    };

    return (
        <Layout>

            {message && <div className="message">{message}</div>}

            <div className="purchase-form-page">

                <h1>Receive Inventory</h1>

                <form onSubmit={handleSubmit}>

                    <div className="form-group">
                        <label>Select Product</label>

                        <select
                            value={productId}
                            onChange={(e) => setProductId(e.target.value)}
                            required
                        >
                            <option value="">Select a product</option>
                            {products.map((product) => (
                                <option key={product.id} value={product.id}>
                                    {product.name}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="form-group">
                        <label>Select Supplier</label>

                        <select
                            value={supplierId}
                            onChange={(e) => setSupplierId(e.target.value)}
                            required
                        >
                            <option value="">Select a supplier</option>
                            {suppliers.map((supplier) => (
                                <option key={supplier.id} value={supplier.id}>
                                    {supplier.name}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="form-group">
                        <label>Description</label>
                        <input
                            type="text"
                            value={description}
                            onChange={(e) => setDescription(e.target.value)}
                        />
                    </div>

                    <div className="form-group">
                        <label>Note</label>
                        <input
                            type="text"
                            value={note}
                            onChange={(e) => setNote(e.target.value)}
                        />
                    </div>

                    <div className="form-group">
                        <label>Quantity</label>
                        <input
                            type="number"
                            value={quantity}
                            onChange={(e) => setQuantity(e.target.value)}
                            required
                        />
                    </div>

                    <button
                        type="submit"
                        disabled={!productId || !supplierId || !quantity}
                    >
                        Purchase Product
                    </button>

                </form>
            </div>

        </Layout>
    );
};

export default PurchasePage;