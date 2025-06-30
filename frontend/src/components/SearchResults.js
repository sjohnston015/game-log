import React from 'react';

const SearchResults = () => {
  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-900 via-gray-800 to-gray-900">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20">
        <div className="text-center">
          <h1 className="text-4xl font-bold text-white mb-6">
            Search Results
          </h1>
          <p className="text-gray-300 text-lg">
            This is where search results will be displayed.
          </p>
          <p className="text-gray-400 text-sm mt-4">
            Coming soon: Games from RAWG API will appear here!
          </p>
        </div>
      </div>
    </div>
  );
};

export default SearchResults;