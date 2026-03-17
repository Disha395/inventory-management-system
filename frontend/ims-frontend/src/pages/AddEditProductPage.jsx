import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from "react-router-dom";
import ApiService from "../services/ApiService";
import Layout from "../components/Layout";

const AddEditProductPage = () => {

    const { productId } = useParams();

    const [name, setName] = useState("");
    const [sku, setSku] = useState("");
    const [price, setPrice] = useState("");
    const [stockQuantity, setStockQuantity] = useState("");
    const [categoryId, setCategoryId] = useState("");
    const [description, setDescription] = useState("");
    const [imageFile, setImageFile] = useState(null);
    const [imageUrl, setImageUrl] = useState("");
    const [isEditing, setIsEditing] = useState(false);
    const [categories, setCategories] = useState([]);
    const [message, setMessage] = useState("");

    const navigate = useNavigate();

    useEffect(() => {

        // Fetch categories
        const fetchCategories = async () => {
            try {
                const res = await ApiService.getAllCategories();
                if (res.status === 200) {
                    setCategories(res.categories);
                }
            } catch (error) {
                showMessage("Error fetching categories");
            }
        };

        // Fetch product (only in edit mode)
        const fetchProductById = async () => {
            try {
                const res = await ApiService.getProductById(productId);
                if (res.status === 200) {

                    const p = res.product;

                    setName(p.name);
                    setSku(p.sku);
                    setPrice(p.price);
                    setStockQuantity(p.stockQuantity);
                    setCategoryId(p.categoryId);
                    setDescription(p.description);
                    setImageUrl(p.imageUrl);
                    setIsEditing(true);
                }
            } catch (error) {
                showMessage("Error fetching product");
            }
        };

        fetchCategories();

        if (productId) {
            fetchProductById();
        }

    }, [productId]);


    const showMessage = (msg) => {
        setMessage(msg);
        setTimeout(() => setMessage(""), 4000);
    };


    const handleImageChange = (e) => {

        const file = e.target.files[0];
        if (!file) return;

        setImageFile(file);

        const reader = new FileReader();
        reader.onloadend = () => setImageUrl(reader.result);
        reader.readAsDataURL(file);
    };


    const handleSubmit = async (e) => {

        e.preventDefault();

        const formData = new FormData();

        formData.append("name", name);
        formData.append("sku", sku);
        formData.append("price", price);
        formData.append("stockQuantity", stockQuantity);
        formData.append("categoryId", categoryId);
        formData.append("description", description);

        if (imageFile) {
            formData.append("image", imageFile);
        }

        try {

            if (isEditing) {

                formData.append("productId", productId);

                await ApiService.updateProduct(formData);

                showMessage("Product updated successfully");

            } else {

                await ApiService.addProduct(formData);

                showMessage("Product added successfully");
            }

            navigate("/products");

        } catch (error) {

            showMessage(
                error.response?.data?.message || "Error saving product"
            );
        }
    };


    return (
        <Layout>

            {message && <div className="message">{message}</div>}

            <div className="product-form-page">

                <h1>{isEditing ? "Edit Product" : "Add Product"}</h1>

                <form onSubmit={handleSubmit}>

                    <input
                        type="text"
                        placeholder="Name"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        required
                    />

                    <input
                        type="text"
                        placeholder="SKU"
                        value={sku}
                        onChange={(e) => setSku(e.target.value)}
                        required
                    />

                    <input
                        type="number"
                        placeholder="Stock Quantity"
                        value={stockQuantity}
                        onChange={(e) => setStockQuantity(e.target.value)}
                        required
                    />

                    <input
                        type="number"
                        placeholder="Price"
                        value={price}
                        onChange={(e) => setPrice(e.target.value)}
                        required
                    />

                    <textarea
                        placeholder="Description"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                    />

                    <select
                        value={categoryId}
                        onChange={(e) => setCategoryId(e.target.value)}
                        required
                    >
                        <option value="">Select Category</option>
                        {categories.map((c) => (
                            <option key={c.id} value={c.id}>
                                {c.name}
                            </option>
                        ))}
                    </select>

                    <input type="file" onChange={handleImageChange} />

                    {imageUrl && (
                        <img src={imageUrl} alt="preview" className="image-preview" />
                    )}

                    <button type="submit">
                        {isEditing ? "Update Product" : "Add Product"}
                    </button>

                </form>

            </div>

        </Layout>
    );
};

export default AddEditProductPage;