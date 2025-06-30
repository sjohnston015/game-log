import React from 'react';
import { useParams } from 'react-router-dom';

const UserProfile = () => {
  // get username from the URL blehhh
  const { username } = useParams();

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-900 via-gray-800 to-gray-900">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20">
        <div className="text-center">
          <h1 className="text-4xl font-bold text-white mb-6">
            {username}'s Profile
          </h1>
          <p className="text-gray-300 text-lg">
            This is where {username}'s profile information will be displayed.
          </p>
          <p className="text-gray-400 text-sm mt-4">
            Coming soon: Username, email, gaming statistics, and account settings!
          </p>
          <div className="mt-8 p-4 bg-gray-800/30 rounded-lg">
            <p className="text-green-400 text-sm">
              URL Parameter Demo: username = "{username}"
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default UserProfile;