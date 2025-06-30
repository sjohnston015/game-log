import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './App.css';

// --- import page components ---
import Homepage from './components/Homepage';
import SearchResults from './components/SearchResults';
import UserGameLog from './components/UserGameLog';
import UserProfile from './components/UserProfile';

function App() {
  return (
    <div className="App">
      <Router>
        <Routes>
          <Route path="/" element={<Homepage />} />
          <Route path="/search" element={<SearchResults />} />
          <Route path="/users/:username/game-log" element={<UserGameLog />} />
          <Route path="/users/:username" element={<UserProfile />} />
        </Routes>
      </Router>
    </div>
  );
}

export default App;