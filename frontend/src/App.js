import React from 'react';
import './App.css';
import DraggableWindow from './components/DraggableWindow';

function App() {
  return (
    <div className="App">
      {/* Desktop taskbar at top */}
      <div className="taskbar">
        <div className="taskbar-start">
          <div className="start-button">
            Game Log OS
          </div>
        </div>
        <div className="taskbar-time">
          {new Date().toLocaleTimeString()}
        </div>
      </div>

      {/* Desktop area */}
      <div className="desktop">

        {/* Welcome window - draggable */}
        <DraggableWindow
          title="Welcome to Game Log"
          initialX={100}
          initialY={80}
          width={500}
          height={350}
        >
          <div className="welcome-content">
            <h1>Hello gamers</h1>
            <p>Your epic game tracking journey starts now!</p>
            <p>Drag this window around the desktop!</p>
            <button className="button">
              Let's Go!
            </button>
          </div>
        </DraggableWindow>

      </div>

      {/* Desktop taskbar at bottom */}
      <div className="bottom-taskbar">
        <div className="status-indicators">
          <span>Ready</span>
          <span>API: localhost:8080</span>
          <span>{new Date().toLocaleTimeString()}</span>
        </div>
      </div>
    </div>
  );
}

export default App;