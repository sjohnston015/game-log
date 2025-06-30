import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './App.css';

// --- import page components ---
import Layout from './components/Layout';
import Homepage from './components/Homepage';
import SearchResults from './components/SearchResults';
import UserGameLog from './components/UserGameLog';
import UserProfile from './components/UserProfile';

function App() {
  return (
    <div className="App">
      <Router>
        <Layout>
          <Routes>
            <Route path="/" element={<Homepage />} />
            <Route path="/search" element={<SearchResults />} />
            <Route path="/users/:username/game-log" element={<UserGameLog />} />
            <Route path="/users/:username" element={<UserProfile />} />
          </Routes>
        </Layout>
      </Router>
    </div>
  );
}

export default App;