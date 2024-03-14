import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import NotesService from "../services/note.service";

const Note = () => {
  const { id } = useParams();

  const initialNoteState = {
    id: null,
    content: "",
    title: "",
    archived: false,
  };
  const [currentNote, setCurrentNote] = useState(initialNoteState);
  const [message, setMessage] = useState("");
  const [isArchived, setIsArchived] = useState(false);

  const getNote = (id) => {
    NotesService.getNoteById(id)
      .then((response) => {
        const bodyNote = response.data;
        setCurrentNote(bodyNote);
        setIsArchived(bodyNote.archived);
        console.log(response.data);
      })
      .catch((e) => {
        console.log(e);
      });
  };

  useEffect(() => {
    if (id) getNote(id);
  }, []);

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setCurrentNote({ ...currentNote, [name]: value });
  };

  const updateNote = () => {
    currentNote.archived = isArchived;
    NotesService.updateNote(currentNote.id, currentNote)
      .then((response) => {
        console.log(response.data);
        setMessage("The Note was updated successfully!");
      })
      .catch((e) => {
        console.log(e);
      });
  };

  const handleCheckboxChange = () => {
    setIsArchived(!isArchived);
  };

  return (
    <div>
      {currentNote ? (
        <div className="edit-form">
          <h4>Note</h4>
          <form>
            <div className="form-group">
              <label htmlFor="title">Title</label>
              <input
                type="text"
                className="form-control"
                id="title"
                name="title"
                value={currentNote.title}
                onChange={handleInputChange}
              />
            </div>
            <div className="form-group">
              <label htmlFor="content">Content</label>
              <input
                type="text"
                className="form-control"
                id="content"
                name="content"
                value={currentNote.content}
                onChange={handleInputChange}
              />
            </div>

            <div class="form-check">
              <input
                class="form-check-input"
                type="checkbox"
                checked={isArchived}
                id="archiveCheckbox"
                onChange={handleCheckboxChange}
              />
              <label class="form-check-label" for="archiveCheckbox">
                Archive
              </label>
            </div>

            <div className="form-group">
              <label>
                <strong>Status:</strong>
              </label>
              {isArchived ? (
                <span className="badge bg-secondary">Inactive</span>
              ) : (
                <span className="badge bg-success">Active</span>
              )}
            </div>
          </form>
          <button
            type="submit"
            className="badge badge-success"
            onClick={updateNote}
          >
            Update
          </button>{" "}
          <p>{message}</p>
        </div>
      ) : (
        <div>
          <br />
          <p>Please click on a Note...</p>
        </div>
      )}
    </div>
  );
};

export default Note;
