import axios from "axios";
import CryptoJS from "crypto-js";

class ApiService {

    static BASE_URL = import.meta.env.VITE_API_URL;
    static ENCRYPTION_KEY = import.meta.env.VITE_ENCRYPTION_KEY;


    /* ================= ENCRYPTION ================= */

    static encrypt(data) {
        return CryptoJS.AES.encrypt(data, this.ENCRYPTION_KEY).toString();
    }

    static decrypt(data) {
        const bytes = CryptoJS.AES.decrypt(data, this.ENCRYPTION_KEY);
        return bytes.toString(CryptoJS.enc.Utf8);
    }


    /* ================= TOKEN STORAGE ================= */

    static saveToken(token) {
        const encrypted = this.encrypt(token);
        localStorage.setItem("token", encrypted);
    }

    static getToken() {
        const encrypted = localStorage.getItem("token");
        if (!encrypted) return null;
        return this.decrypt(encrypted);
    }

    static saveRole(role) {
        const encrypted = this.encrypt(role);
        localStorage.setItem("role", encrypted);
    }

    static getRole() {
        const encrypted = localStorage.getItem("role");
        if (!encrypted) return null;
        return this.decrypt(encrypted);
    }

    static clearAuth() {
        localStorage.removeItem("token");
        localStorage.removeItem("role");
    }


    /* ================= HEADERS ================= */

    static getHeader() {
        const token = this.getToken();

        return {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json"
        };
    }


    /* ================= AUTH ================= */

    static async loginUser(data) {
        const response = await axios.post(
            `${this.BASE_URL}/auth/login`,
            data
        );
        return response.data;
    }

    static async registerUser(data) {
        const response = await axios.post(
            `${this.BASE_URL}/auth/register`,
            data
        );
        return response.data;
    }


    /* ================= USERS ================= */

    static async getAllUsers() {
        const response = await axios.get(
            `${this.BASE_URL}/users/all`,
            { headers: this.getHeader() }
        );
        return response.data;
    }

    static async getUserById(id) {
        const response = await axios.get(
            `${this.BASE_URL}/users/${id}`,
            { headers: this.getHeader() }
        );
        return response.data;
    }

    static async getCurrentUser() {
        const response = await axios.get(
            `${this.BASE_URL}/users/current`,
            { headers: this.getHeader() }
        );
        return response.data;
    }

    static async updateUser(id, data) {
        const response = await axios.put(
            `${this.BASE_URL}/users/update/${id}`,
            data,
            { headers: this.getHeader() }
        );
        return response.data;
    }

    static async deleteUser(id) {
        const response = await axios.delete(
            `${this.BASE_URL}/users/delete/${id}`,
            { headers: this.getHeader() }
        );
        return response.data;
    }


    /* ================= PRODUCTS ================= */

    static async getAllProducts() {
        const response = await axios.get(
            `${this.BASE_URL}/products`,
            { headers: this.getHeader() }
        );
        return response.data;
    }

    static async getProductById(id) {
        const response = await axios.get(
            `${this.BASE_URL}/products/${id}`,
            { headers: this.getHeader() }
        );
        return response.data;
    }

    static async addProduct(formData) {
        const response = await axios.post(
            `${this.BASE_URL}/products`,
            formData,
            {
                headers: {
                    ...this.getHeader(),
                    "Content-Type": "multipart/form-data"
                }
            }
        );
        return response.data;
    }

    static async updateProduct(formData) {
        const response = await axios.put(
            `${this.BASE_URL}/products`,
            formData,
            {
                headers: {
                    ...this.getHeader(),
                    "Content-Type": "multipart/form-data"
                }
            }
        );
        return response.data;
    }

    static async deleteProduct(id) {
        const response = await axios.delete(
            `${this.BASE_URL}/products/${id}`,
            { headers: this.getHeader() }
        );
        return response.data;
    }

    static async searchProduct(searchValue) {
        const response = await axios.get(
            `${this.BASE_URL}/products/search`,
            {
                params: { searchValue },
                headers: this.getHeader()
            }
        );
        return response.data;
    }


    /* ================= CATEGORY ================= */

    static async createCategory(data) {
        const response = await axios.post(
            `${this.BASE_URL}/categories`,
            data,
            { headers: this.getHeader() }
        );
        return response.data;
    }

    static async getAllCategories() {
        const response = await axios.get(
            `${this.BASE_URL}/categories`,
            { headers: this.getHeader() }
        );
        return response.data;
    }

    static async getCategoryById(id) {
        const response = await axios.get(
            `${this.BASE_URL}/categories/${id}`,
            { headers: this.getHeader() }
        );
        return response.data;
    }

    static async updateCategory(id, data) {
        const response = await axios.put(
            `${this.BASE_URL}/categories/${id}`,
            data,
            { headers: this.getHeader() }
        );
        return response.data;
    }

    static async deleteCategory(id) {
        const response = await axios.delete(
            `${this.BASE_URL}/categories/${id}`,
            { headers: this.getHeader() }
        );
        return response.data;
    }


    /* ================= SUPPLIERS ================= */

    static async getAllSuppliers() {
        const response = await axios.get(
            `${this.BASE_URL}/suppliers`,
            { headers: this.getHeader() }
        );
        return response.data;
    }

    static async getSupplierById(id) {
        const response = await axios.get(
            `${this.BASE_URL}/suppliers/${id}`,
            { headers: this.getHeader() }
        );
        return response.data;
    }

    static async addSupplier(data) {
        const response = await axios.post(
            `${this.BASE_URL}/suppliers`,
            data,
            { headers: this.getHeader() }
        );
        return response.data;
    }

    static async updateSupplier(id, data) {
        const response = await axios.put(
            `${this.BASE_URL}/suppliers/${id}`,
            data,
            { headers: this.getHeader() }
        );
        return response.data;
    }

    static async deleteSupplier(id) {
        const response = await axios.delete(
            `${this.BASE_URL}/suppliers/${id}`,
            { headers: this.getHeader() }
        );
        return response.data;
    }


    /* ================= TRANSACTIONS ================= */

    static async purchaseProduct(data) {
        const response = await axios.post(
            `${this.BASE_URL}/transactions/purchase`,
            data,
            { headers: this.getHeader() }
        );
        return response.data;
    }

    static async sellProduct(data) {
        const response = await axios.post(
            `${this.BASE_URL}/transactions/sell`,
            data,
            { headers: this.getHeader() }
        );
        return response.data;
    }

    static async returnToSupplier(data) {
        const response = await axios.post(
            `${this.BASE_URL}/transactions/return`,
            data,
            { headers: this.getHeader() }
        );
        return response.data;
    }

    static async getAllTransactions(filter) {
        const response = await axios.get(
            `${this.BASE_URL}/transactions/all`,
            {
                headers: this.getHeader(),
                params: { filter }
            }
        );
        return response.data;
    }

    static async getTransactionById(id) {
        const response = await axios.get(
            `${this.BASE_URL}/transactions/${id}`,
            { headers: this.getHeader() }
        );
        return response.data;
    }


    /* ================= AUTH HELPERS ================= */

    static logout() {
        this.clearAuth();
    }

    static isAuthenticated() {
        return !!this.getToken();
    }

    static isAdmin() {
        return this.getRole() === "ADMIN";
    }
}

export default ApiService;