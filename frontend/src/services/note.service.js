import axios from "axios";
import globals from "../common/Globals";

const API_URL = `${globals.host}/v1/notes`;

const getNotes = () => {
  return axios.get(API_URL);
};

const getNoteById = id => {
  return axios.get(`${API_URL}/${id}`);
};

const getNotesFiltered = (archived = false) => {
  return axios.get(API_URL, {
    params: {
      archived: archived,
    },
  });
};

const saveNote = data => {
  return axios.post(`${API_URL}`, data);
};


const deleteNote = id => {
  return axios.delete(`${API_URL}/${id}`);
};

const updateNote = (id, data) => {
  return axios.put(`${API_URL}/${id}`, data);
};

const addCategoryToNote = (id, data) => {
  return axios.post(`${API_URL}/${id}/addCategories`, data);
};

const removeCategoryFromNote = (id, data) => {
  return axios.post(`${API_URL}/${id}/removeCategories`, data);
};

const NotesService = {
  getNotes,
  getNoteById,
  getNotesFiltered,
  saveNote,
  deleteNote,
  updateNote,
  addCategoryToNote,
  removeCategoryFromNote,
};

export default NotesService;

