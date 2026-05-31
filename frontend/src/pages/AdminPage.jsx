import React, { useState, useEffect } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { api } from '../api/api'
import { useAuth } from '../context/AuthContext'
import AdminAnimeManagerPage from './AdminAnimeManagerPage'

export default function AdminPage() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()
  const [activeTab, setActiveTab] = useState('invitations')

  const handleLogout = () => {
    localStorage.clear()
    logout()
    navigate('/login')
  }

  return (
    <div className="min-h-screen bg-gradient-to-b from-slate-900 via-slate-800 to-slate-900">
      {/* Header */}
      <header className="sticky top-0 z-50 bg-black/80 backdrop-blur-md border-b border-orange-500/20">
        <div className="max-w-7xl mx-auto px-4 py-4 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 bg-gradient-to-br from-red-600 to-orange-600 rounded-lg flex items-center justify-center">
              <span className="text-white font-bold text-lg">⚙️</span>
            </div>
            <h1 className="text-2xl font-bold bg-gradient-to-r from-red-500 to-orange-500 bg-clip-text text-transparent">
              Admin Panel
            </h1>
          </div>

          <nav className="flex items-center gap-6">
            <Link to="/" className="px-4 py-2 rounded-lg text-gray-300 hover:bg-white/10 transition-colors">
              ← Dashboard
            </Link>
            <div className="flex items-center gap-3 pl-6 border-l border-orange-500/20">
              <span className="text-sm text-gray-400">Admin: {user?.email}</span>
              <button
                onClick={handleLogout}
                className="px-4 py-2 rounded-lg bg-red-500/10 text-red-400 hover:bg-red-500/20 transition-colors font-medium"
              >
                Logout
              </button>
            </div>
          </nav>
        </div>
      </header>

      <main className="max-w-7xl mx-auto px-4 py-12">
        {/* Tab Navigation */}
        <div className="flex gap-2 mb-8 border-b border-orange-500/20">
          <button
            onClick={() => setActiveTab('invitations')}
            className={`px-6 py-3 font-medium border-b-2 transition-colors ${
              activeTab === 'invitations'
                ? 'border-orange-500 text-orange-400'
                : 'border-transparent text-gray-400 hover:text-gray-300'
            }`}
          >
            📧 Einladungen
          </button>
          <button
            onClick={() => setActiveTab('anime')}
            className={`px-6 py-3 font-medium border-b-2 transition-colors ${
              activeTab === 'anime'
                ? 'border-orange-500 text-orange-400'
                : 'border-transparent text-gray-400 hover:text-gray-300'
            }`}
          >
            🎬 Anime Manager
          </button>
        </div>

        {/* Anime Manager Tab */}
        {activeTab === 'anime' && <AdminAnimeManagerPage />}

        {/* Invitations Tab */}
        {activeTab === 'invitations' && (
          <div className="bg-slate-800 rounded-xl border border-orange-500/20 p-6">
            <h3 className="text-lg font-bold text-white mb-6">📧 Neue Einladung erstellen</h3>
            <p className="text-gray-400">Einladungs-Feature wird in der nächsten Version hinzugefügt</p>
          </div>
        )}
      </main>
    </div>
  )
}
