import React, { useEffect, useState } from "react";
import ApiService from "../service/ApiService";
import Layout from "../component/Layout";

const CategoryPage = () => {

    const [categories, setCategories] = useState([]);
    const [categoryName, setCategoryName] = useState("");
    const [message, setMessage] = useState("");
    const [isEditing, setIsEditing] = useState(false);
    const [editingCategoryId, setEditingCategoryId] = useState(null);

    const fetchCategories = async () => {
        try {
            const response = await ApiService.getAllCategories();
            if (response.status === 200) {
                setCategories(response.categories);
            }
        } catch (error) {
            showMessage(
                error.response?.data?.message || "Error fetching categories"
            );
        }
    };

    useEffect(() => {
        fetchCategories();
    }, []);

    const addCategory = async () => {
        if (!categoryName) {
            showMessage("Category name is required");
            return;
        }

        try {
            await ApiService.createCategory({ name: categoryName });

            showMessage("Category added successfully");
            setCategoryName("");

            fetchCategories();

        } catch (error) {
            showMessage(
                error.response?.data?.message || "Error adding category"
            );
        }
    };

    const editCategory = async () => {
        if (!categoryName) {
            showMessage("Category name is required");
            return;
        }

        try {
            await ApiService.updateCategory(editingCategoryId, { name: categoryName });

            showMessage("Category updated successfully");

            setIsEditing(false);
            setEditingCategoryId(null);
            setCategoryName("");

            fetchCategories();

        } catch (error) {
            showMessage(
                error.response?.data?.message || "Error updating category"
            );
        }
    };

    const handleEditCategory = (category) => {
        setIsEditing(true);
        setEditingCategoryId(category.id);
        setCategoryName(category.name);
    };

    const handleDeleteCategory = async (categoryId) => {
        if (window.confirm("Are you sure you want to delete this category?")) {
            try {
                await ApiService.deleteCategory(categoryId);

                showMessage("Category deleted successfully");

                fetchCategories();

            } catch (error) {
                showMessage(
                    error.response?.data?.message || "Error deleting category"
                );
            }
        }
    };

    const showMessage = (msg) => {
        setMessage(msg);
        setTimeout(() => setMessage(""), 4000);
    };

    return (
        <Layout>
            {message && <div className="message">{message}</div>}

            <div className="category-page">

                <div className="category-header">

                    <h1>Categories</h1>

                    <div className="add-cat">
                        <input
                            value={categoryName}
                            type="text"
                            placeholder="Category Name"
                            onChange={(e) => setCategoryName(e.target.value)}
                        />

                        {isEditing ? (
                            <button onClick={editCategory}>
                                Update Category
                            </button>
                        ) : (
                            <button onClick={addCategory}>
                                Add Category
                            </button>
                        )}
                    </div>
                </div>

                {categories.length > 0 ? (
                    <ul className="category-list">
                        {categories.map((category) => (
                            <li className="category-item" key={category.id}>
                                <span>{category.name}</span>

                                <div className="category-actions">
                                    <button onClick={() => handleEditCategory(category)}>
                                        Edit
                                    </button>
                                    <button onClick={() => handleDeleteCategory(category.id)}>
                                        Delete
                                    </button>
                                </div>
                            </li>
                        ))}
                    </ul>
                ) : (
                    <p>No categories found</p>
                )}
            </div>
        </Layout>
    );
};

export default CategoryPage;