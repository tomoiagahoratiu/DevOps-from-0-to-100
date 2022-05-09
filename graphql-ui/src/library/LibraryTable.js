import React, { useEffect, useState } from "react";
import { request, gql } from "graphql-request";

const getBooksQuery = gql`
  query {
    books {
      id
      name
    }
  }
`;

const addBooksQuery = gql`
  mutation ($name: String!) {
    saveBook(book: { name: $name }) {
      id
      name
    }
  }
`;

const deleteBooksQuery = gql`
  mutation ($id: Long!) {
    deleteBook(id: $id)
  }
`;

const LibraryTable = () => {
  const [listOfBooks, setListOfBooks] = useState([]);
  const [isAddFormActive, setIsAddFormActive] = useState(false);
  const [updateStatus, setUpdateStatus] = useState(false);
  const [nameOfNewBook, setNameOfNewBook] = useState();

  useEffect(() => {
    const timeout = setTimeout(() => {
      request("/graphql", getBooksQuery).then((data) =>
        setListOfBooks(data.books)
      );
    }, 300);
  }, [updateStatus]);

  const addBook = (name) => {
    request("/graphql", addBooksQuery, { name: name });
    setUpdateStatus(!updateStatus);
    setNameOfNewBook("");
  };

  const deteleBook = (id) => {
    request("/graphql", deleteBooksQuery, { id: id });
    setUpdateStatus(!updateStatus);
  };

  return (
    <>
      <table>
        <thead>
          <tr>
            <th>Id</th>
            <th>Name</th>
          </tr>
        </thead>
        <tbody>
          {listOfBooks.map((book) => (
            <tr>
              <td>{book.id}</td>
              <td>{book.name}</td>
              <button onClick={() => deteleBook(book.id)}>Delete</button>
            </tr>
          ))}
        </tbody>
      </table>
      <button onClick={() => setIsAddFormActive(!isAddFormActive)}>
        Add Book
      </button>
      {isAddFormActive && (
        <div>
          <input
            value={nameOfNewBook}
            onChange={(e) => setNameOfNewBook(e.target.value)}
          ></input>
          <button onClick={() => addBook(nameOfNewBook)}>Save</button>
        </div>
      )}
    </>
  );
};

export default LibraryTable;
