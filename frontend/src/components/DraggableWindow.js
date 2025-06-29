import React, { useState, useRef } from 'react';

function DraggableWindow({ title, children, initialX = 100, initialY = 100, width = 400, height = 300 }) {
  // state for window position
  const [position, setPosition] = useState({ x: initialX, y: initialY });
  const [isDragging, setIsDragging] = useState(false);
  const [dragStart, setDragStart] = useState({ x: 0, y: 0 });

  // ref to the window element
  const windowRef = useRef(null);

  // handle mouse down on titlebar (start dragging)
  const handleMouseDown = (e) => {
    setIsDragging(true);
    setDragStart({
      x: e.clientX - position.x,
      y: e.clientY - position.y
    });

    // prevent text selection while dragging
    e.preventDefault();
  };

  // handle mouse move (dragging)
  const handleMouseMove = (e) => {
    if (!isDragging) return;

    const newX = e.clientX - dragStart.x;
    const newY = e.clientY - dragStart.y;

    // keep window within desktop bounds
    const maxX = window.innerWidth - width;
    const maxY = window.innerHeight - height - 60; // account for taskbars

    setPosition({
      x: Math.max(0, Math.min(newX, maxX)),
      y: Math.max(30, Math.min(newY, maxY)) // keep below top taskbar
    });
  };

  // handle mouse up (stop dragging)
  const handleMouseUp = () => {
    setIsDragging(false);
  };

  // add global mouse event listeners when dragging
  React.useEffect(() => {
    if (isDragging) {
      document.addEventListener('mousemove', handleMouseMove);
      document.addEventListener('mouseup', handleMouseUp);
      document.body.style.cursor = 'grabbing';
      document.body.style.userSelect = 'none';
    } else {
      document.removeEventListener('mousemove', handleMouseMove);
      document.removeEventListener('mouseup', handleMouseUp);
      document.body.style.cursor = 'default';
      document.body.style.userSelect = 'auto';
    }

    // cleanup function
    return () => {
      document.removeEventListener('mousemove', handleMouseMove);
      document.removeEventListener('mouseup', handleMouseUp);
      document.body.style.cursor = 'default';
      document.body.style.userSelect = 'auto';
    };
  }, [isDragging, dragStart, position]);

  return (
    <div
      ref={windowRef}
      className="draggable-window"
      style={{
        left: position.x,
        top: position.y,
        width: width,
        height: height
      }}
    >
      {/* window titlebar - this is what you drag */}
      <div
        className="window-titlebar-draggable"
        onMouseDown={handleMouseDown}
      >
        <span className="window-title">{title}</span>
        <div className="window-controls">
          <button className="control-btn minimize">_</button>
          <button className="control-btn maximize">□</button>
          <button className="control-btn close">×</button>
        </div>
      </div>

      {/* window content */}
      <div className="window-content-area">
        {children}
      </div>
    </div>
  );
}

export default DraggableWindow;