import React from 'react';
import './App.css';
import Homepage from './components/Homepage';

function App() {
  return (
    <div className="App">
      {/*
        Adding routing later like this:

        <Router>
          <Routes>
            <Route path="/" element={<Homepage />} />
            <Route path="/search" element={<GameSearch />} />
            <Route path="/library" element={<UserLibrary />} />
          </Routes>
        </Router>
      */}

      <Homepage />
    </div>
  );
}

export default App;