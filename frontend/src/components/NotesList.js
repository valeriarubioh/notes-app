import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";

import NotesService from "../services/note.service";

const NotesList = () => {
  const [notes, setNotes] = useState("");
  const [newNote, setNewNote] = useState({
    title: "",
    content: "",
    archived: false,
  });
  const [showArchived, setShowArchived] = useState(null);

  useEffect(() => {
    if (showArchived === null) {
      NotesService.getNotes().then(
        (response) => {
          setNotes(response.data);
        },
        (error) => {
          console.error(error);
        }
      );
    } else {
      NotesService.getNotesFiltered(showArchived).then(
        (response) => {
          setNotes(response.data);
        },
        (error) => {
          console.error(error);
        }
      );
    }
  }, [showArchived]);

  const handleSaveNote = (event) => {
    event.preventDefault();
    NotesService.saveNote(newNote).then(
      (response) => {
        console.log("Note saved successfully!");
        setNotes([...notes, response.data]);
        setNewNote({ title: "", content: "", archived: false });
      },
      (error) => {
        console.error("Error saving note:", error);
      }
    );
  };

  const deleteNote = (id) => {
    NotesService.deleteNote(id)
      .then((response) => {
        if (response.status === 204) {
          setNotes(notes.filter((note) => note.id !== id));
        }
      })
      .catch((e) => {
        console.log(e);
      });
  };

  return (
    <div className="container">
      <div>
        <button
          type="button"
          className="btn btn-primary mr-2"
          onClick={() => setShowArchived(null)}
        >
          Show All
        </button>
        <button
          type="button"
          className="btn btn-success mr-2"
          onClick={() => setShowArchived(false)}
        >
          Show Active
        </button>
        <button
          type="button"
          className="btn btn-warning"
          onClick={() => setShowArchived(true)}
        >
          Show Archived
        </button>
      </div>
      <form className="edit-form" onSubmit={handleSaveNote}>
        <div className="form-group">
          <label>Title:</label>
          <input
            className="form-control"
            type="text"
            value={newNote.title}
            onChange={(e) => setNewNote({ ...newNote, title: e.target.value })}
          />
        </div>
        <div className="form-group">
          <label>Content:</label>
          <input
            className="form-control"
            type="text"
            value={newNote.content}
            onChange={(e) =>
              setNewNote({ ...newNote, content: e.target.value })
            }
          />
        </div>
        <div className="form-group">
          <label>Archived:</label>
          <input
            className="form-control"
            type="checkbox"
            checked={newNote.archived}
            onChange={() =>
              setNewNote({ ...newNote, archived: !newNote.archived })
            }
          />
        </div>
        <button type="submit" class="btn btn-primary">
          Save Note
        </button>
      </form>
      <br />
      <table className="table">
        <thead className="thead-dark">
          <tr>
            <th scope="col">#</th>
            <th scope="col">Title</th>
            <th scope="col">Content</th>
            <th scope="col">Status</th>
            <th scope="col">Actions</th>
          </tr>
        </thead>
        <tbody>
          {notes &&
            notes.map((note) => (
              <tr key={note.id}>
                <td>{note.id}</td>
                <td>{note.title}</td>
                <td>{note.content}</td>
                <td>
                  {note.archived ? (
                    <span className="badge bg-secondary">Inactive</span>
                  ) : (
                    <span className="badge bg-success">Active</span>
                  )}
                </td>
                <td>
                  <Link
                    to={"/notes/" + note.id}
                    className="badge badge-warning"
                  >
                    Edit
                  </Link>{" "}
                  <button
                    className="badge badge-danger mr-2"
                    onClick={() => deleteNote(note.id)}
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
        </tbody>
      </table>
    </div>
  );
};

export default NotesList;
