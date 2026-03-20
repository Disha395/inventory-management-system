import React, { useEffect, useState } from 'react'
import ApiService from "../service/ApiService.js";
import Layout from "../component/Layout.jsx";

const SellPage = () => {

    const [products, setProducts] = useState([]);
    const [productId, setProductId] = useState("");
    const [description, setDescription] = useState("");
    const [note, setNote] = useState("");
    const [quantity, setQuantity] = useState("");
    const [message, setMessage] = useState("");

    useEffect(() => {

        const fetchProducts = async () => {
            try {
                const productData = await ApiService.getAllProducts();
                setProducts(productData?.products || []);
            } catch (error) {
                showMessage(
                    error.response?.data?.message || "Error fetching products"
                );
            }
        };

        fetchProducts();

    }, []);

    const handleSubmit = async (e) => {

        e.preventDefault();

        if (!productId || !quantity) {
            showMessage("Please fill in all fields");
            return;
        }

        const body = {
            productId,
            quantity: Number(quantity),
            description,
            note,
        };

        try {

            const response = await ApiService.sellProduct(body);

            showMessage(response.message);
            resetForm();

        } catch (error) {

            showMessage(
                error.response?.data?.message || "Error selling product"
            );
        }
    };

    const resetForm = () => {
        setProductId("");
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

            <div className="sell-form-page">

                <h1>Sell Product</h1>

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
                        <label>Quantity</label>
                        <input
                            type="number"
                            value={quantity}
                            onChange={(e) => setQuantity(e.target.value)}
                            required
                        />
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

                    <button
                        type="submit"
                        disabled={!productId || !quantity}
                    >
                        Sell Product
                    </button>

                </form>

            </div>

        </Layout>
    );
};

export default SellPage;