import { Navigate, useLocation } from "react-router-dom";
import ApiService from "../services/ApiService";

export const ProtectedRoute = ({ element }) => {
    const location = useLocation();

    if (!ApiService.isAuthenticated()) {
        return <Navigate to="/login" replace state={{ from: location }} />;
    }

    return element;
};

export const AdminRoute = ({ element }) => {
    const location = useLocation();

    if (!ApiService.isAuthenticated() || !ApiService.isAdmin()) {
        return <Navigate to="/login" replace state={{ from: location }} />;
    }

    return element;
};