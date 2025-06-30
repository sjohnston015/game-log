import React, { useState, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';

const SearchResults = () => {
  // get the search query from URL parameters
  const [searchParams] = useSearchParams();
  const query = searchParams.get('q') || '';

  // state for managing search results and UI
  const [games, setGames] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // function to call backend API
  const searchGames = async (searchQuery) => {
    if (!searchQuery.trim()) return;

    setLoading(true);
    setError(null);

    try {
      // search functionality from my backend
      const response = await fetch(`http://localhost:8080/api/games/search?q=${encodeURIComponent(searchQuery)}`);

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const data = await response.json();
      setGames(data.results || []); // backend returns - results: [...]
    } catch (err) {
      setError(err.message);
      console.error('Search error:', err);
    } finally {
      setLoading(false);
    }
  };

  // search when component loads or query changes
  useEffect(() => {
    if (query) {
      searchGames(query);
    }
  }, [query]);

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      {/* Search Header */}
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-white mb-2">
          Search Results
        </h1>
        {query && (
          <p className="text-gray-300">
            Showing results for: <span className="text-green-400 font-semibold">"{query}"</span>
          </p>
        )}
      </div>

      {/* Loading State */}
      {loading && (
        <div className="text-center py-12">
          <div className="inline-block animate-spin rounded-full h-8 w-8 border-b-2 border-green-400"></div>
          <p className="text-gray-300 mt-4">Searching for games...</p>
        </div>
      )}

      {/* Error State */}
      {error && (
        <div className="bg-red-900/20 border border-red-500/50 rounded-lg p-6 mb-8">
          <h3 className="text-red-400 font-semibold mb-2">Search Error</h3>
          <p className="text-gray-300">
            Failed to search games: {error}
          </p>
          <button
            onClick={() => searchGames(query)}
            className="mt-4 px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors"
          >
            Try Again
          </button>
        </div>
      )}

      {/* Results */}
      {!loading && !error && games.length > 0 && (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
          {games.map((game) => (
            <div
              key={game.id}
              className="bg-gray-800/50 rounded-lg overflow-hidden hover:bg-gray-800/70 transition-all duration-300 border border-gray-700/50 hover:border-green-500/30"
            >
              {/* Game Image */}
              <div className="aspect-video bg-gray-700">
                {game.background_image ? (
                  <img
                    src={game.background_image}
                    alt={game.name}
                    className="w-full h-full object-cover"
                  />
                ) : (
                  <div className="w-full h-full flex items-center justify-center text-gray-500">
                    No Image
                  </div>
                )}
              </div>

              {/* Game Info */}
              <div className="p-4">
                <h3 className="text-white font-semibold text-lg mb-2 line-clamp-2">
                  {game.name}
                </h3>

                {/* Rating */}
                {game.rating && (
                  <div className="flex items-center mb-2">
                    <span className="text-green-400 font-medium">{game.rating}</span>
                    <span className="text-gray-400 text-sm ml-1">/ 5.0</span>
                  </div>
                )}

                {/* Platforms */}
                {game.platforms && game.platforms.length > 0 && (
                  <div className="flex flex-wrap gap-1 mb-3">
                    {game.platforms.slice(0, 3).map((platformData, index) => (
                      <span
                        key={index}
                        className="text-xs bg-gray-700 text-gray-300 px-2 py-1 rounded"
                      >
                        {platformData.platform.name}
                      </span>
                    ))}
                    {game.platforms.length > 3 && (
                      <span className="text-xs text-gray-500">
                        +{game.platforms.length - 3} more
                      </span>
                    )}
                  </div>
                )}

                {/* Add to Library Button */}
                <button className="w-full bg-gradient-to-r from-green-500 to-emerald-600 text-white py-2 px-4 rounded-lg hover:from-green-600 hover:to-emerald-700 transition-all duration-200 font-medium">
                  Add to Library
                </button>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* No Results */}
      {!loading && !error && games.length === 0 && query && (
        <div className="text-center py-12">
          <h3 className="text-xl font-semibold text-gray-300 mb-2">No games found</h3>
          <p className="text-gray-400">
            Try searching for a different game title
          </p>
        </div>
      )}

      {/* No Query */}
      {!query && (
        <div className="text-center py-12">
          <h3 className="text-xl font-semibold text-gray-300 mb-2">Search for games</h3>
          <p className="text-gray-400">
            Use the search bar above to find games
          </p>
        </div>
      )}
    </div>
  );
};

export default SearchResults;