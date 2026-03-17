import React, { useEffect, useState } from 'react';
import { useNavigate } from "react-router-dom";
import ApiService from "../service/ApiService";
import Layout from "../component/Layout";
import PaginationComponent from "../component/PaginationComponent";

const ProductPage = () => {

    const [products, setProducts] = useState([]);
    const [allProducts, setAllProducts] = useState([]);
    const [message, setMessage] = useState("");

    const navigate = useNavigate();

    const [currentPage, setCurrentPage] = useState(1);
    const [totalPages, setTotalPages] = useState(0);
    const itemsPerPage = 10;

    useEffect(() => {

        const getProducts = async () => {
            try {

                const productData = await ApiService.getAllProducts();

                if (productData.status === 200) {

                    setAllProducts(productData.products);
                    setTotalPages(Math.ceil(productData.products.length / itemsPerPage));

                }

            } catch (error) {

                showMessage(
                    error.response?.data?.message || "Error loading products"
                );

            }
        };

        getProducts();

    }, []);

    useEffect(() => {

        setProducts(
            allProducts.slice(
                (currentPage - 1) * itemsPerPage,
                currentPage * itemsPerPage
            )
        );

    }, [currentPage, allProducts]);


    const handleDeleteProduct = async (productId) => {

        if (window.confirm("Are you sure you want to delete this product?")) {

            try {

                await ApiService.deleteProduct(productId);

                setAllProducts(prev => prev.filter(p => p.id !== productId));

                showMessage("Product deleted successfully");

            } catch (error) {

                showMessage(
                    error.response?.data?.message || "Error deleting product"
                );

            }
        }
    };


    const showMessage = (msg) => {

        setMessage(msg);

        setTimeout(() => {
            setMessage("");
        }, 4000);
    };


    return (
        <Layout>

            {message && <div className="message">{message}</div>}

            <div className="product-page">

                <div className="product-header">

                    <h1>Products</h1>

                    <button
                        className="add-product-btn"
                        onClick={() => navigate("/add-product")}
                    >
                        Add Product
                    </button>

                </div>

                {products.length === 0 ? (

                    <p>No products available</p>

                ) : (

                    <div className="product-list">

                        {products.map((product) => (

                            <div key={product.id} className="product-item">

                                <img
                                    className="product-image"
                                    src={product.imageUrl}
                                    alt={product.name}
                                />

                                <div className="product-info">

                                    <h3>{product.name}</h3>
                                    <p>SKU: {product.sku}</p>
                                    <p>Price: {product.price}</p>
                                    <p>Quantity: {product.stockQuantity}</p>

                                </div>

                                <div className="product-actions">

                                    <button onClick={() => navigate(`/edit-product/${product.id}`)}>
                                        Edit
                                    </button>

                                    <button onClick={() => handleDeleteProduct(product.id)}>
                                        Delete
                                    </button>

                                </div>

                            </div>
                        ))}

                    </div>
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

export default ProductPage;