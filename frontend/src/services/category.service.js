import axios from "axios";
import globals from "../common/Globals";

const API_URL = `${globals.host}/v1/categories`;

const getCategories = () => {
  return axios.get(API_URL);
};

const createCategory = data => {
  return axios.post(`${API_URL}`, data);
};
const updateCategory = (id, data) => {
  return axios.put(`${API_URL}/${id}`, data);
};
const deleteCategory = id => {
  return axios.delete(`${API_URL}/${id}`);
};

const CategoryService = {
  getCategories,
  createCategory,
  updateCategory,
  deleteCategory
};

export default CategoryService;
