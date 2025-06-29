import React from 'react';
import './App.css';

function App() {
  return (
    <div className="App">
      <header className="header">
        <div className="window-titlebar">
          <div className="titlebar-text"> Game Logy</div>
          <div className="window-controls">
            <button className="control-btn minimize">_</button>
            <button className="control-btn maximize">□</button>
            <button className="control-btn close">×</button>
          </div>
        </div>
      </header>

      <main className="main-content">
        <div className="window">
          <div className="window-header">
            Welcome to Your Game Log
          </div>
          <div className="window-content">
            <h1>Hello gamers!</h1>
            <p>Your epic game tracking journey starts now</p>
            <button className="button">
              Let's Go! 
            </button>
          </div>
        </div>
      </main>

      <footer className="footer">
        <div className="status-bar">
          <span>🌟 Status: Ready to Game</span>
          <span>📊 API: localhost:8080</span>
          <span>⏰ {new Date().toLocaleTimeString()}</span>
        </div>
      </footer>
    </div>
  );
}

export default App;