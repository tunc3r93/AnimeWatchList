import React, { useState, useEffect } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { api } from '../api/api'
import { useAuth } from '../context/AuthContext'

export default function DashboardPage() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()
  const [anime, setAnime] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [search, setSearch] = useState('')
  const [selectedGenre, setSelectedGenre] = useState('')
  const [selectedStatus, setSelectedStatus] = useState('')
  const [sortBy, setSortBy] = useState('title')
  const [addingToWatchlist, setAddingToWatchlist] = useState(null)

  const genres = ['Action', 'Drama', 'Comedy', 'Romance', 'Thriller', 'Slice of Life']
  const statuses = ['AIRING', 'COMPLETED', 'UPCOMING']

  useEffect(() => {
    loadAnime()
  }, [search, selectedGenre, selectedStatus, sortBy])

  const loadAnime = async () => {
    setLoading(true)
    try {
      const data = await api.getAnime(0, 50, search, selectedGenre, selectedStatus)
      let animeList = Array.isArray(data) ? data : data?.content || []
      
      // Sort
      animeList.sort((a, b) => {
        switch(sortBy) {
          case 'title': return a.title.localeCompare(b.title)
          case 'year': return (b.releaseYear || 0) - (a.releaseYear || 0)
          case 'rating': return (b.avgRating || 0) - (a.avgRating || 0)
          case 'episodes': return (b.episodeCount || 0) - (a.episodeCount || 0)
          default: return 0
        }
      })
      
      setAnime(animeList)
      setError('')
    } catch (err) {
      setError(err.message)
      console.error('Error loading anime:', err)
    } finally {
      setLoading(false)
    }
  }

  const handleAddToWatchlist = async (animeId) => {
    setAddingToWatchlist(animeId)
    try {
      await api.addToWatchlist(animeId, 'PLAN_TO_WATCH')
      setSuccess('✅ Zur Watchlist hinzugefügt!')
      setTimeout(() => setSuccess(''), 3000)
    } catch (err) {
      setError('❌ Fehler beim Hinzufügen: ' + err.message)
      setTimeout(() => setError(''), 3000)
    } finally {
      setAddingToWatchlist(null)
    }
  }

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
            <div className="w-10 h-10 bg-gradient-to-br from-orange-500 to-red-600 rounded-lg flex items-center justify-center">
              <span className="text-white font-bold text-lg">A</span>
            </div>
            <h1 className="text-2xl font-bold bg-gradient-to-r from-orange-500 to-red-500 bg-clip-text text-transparent">
              AnimeWatch
            </h1>
          </div>

          <nav className="flex items-center gap-6">
            {user?.role === 'ADMIN' && (
              <Link
                to="/admin"
                className="px-4 py-2 rounded-lg bg-orange-500/10 text-orange-400 hover:bg-orange-500/20 transition-colors font-medium"
              >
                ⚙️ Admin
              </Link>
            )}
            <Link
              to="/watchlist"
              className="px-4 py-2 rounded-lg text-gray-300 hover:bg-white/10 transition-colors"
            >
              📺 Watchlist
            </Link>
            <div className="flex items-center gap-3 pl-6 border-l border-orange-500/20">
              <span className="text-sm text-gray-400">{user?.email}</span>
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

      {/* Hero Section */}
      <section className="py-12 px-4 bg-gradient-to-r from-orange-600/20 to-red-600/20 border-b border-orange-500/20">
        <div className="max-w-7xl mx-auto">
          <h2 className="text-4xl font-bold text-white mb-4">Entdecke Anime</h2>
          <p className="text-gray-300 text-lg">Tausende Anime zum Streamen und Verwalten</p>
        </div>
      </section>

      <main className="max-w-7xl mx-auto px-4 py-12">
        {/* Notifications */}
        {error && (
          <div className="mb-6 p-4 rounded-lg bg-red-500/10 border border-red-500/50 text-red-300 flex items-center justify-between">
            <div>{error}</div>
            <button onClick={() => setError('')} className="text-red-400 hover:text-red-300">✕</button>
          </div>
        )}
        {success && (
          <div className="mb-6 p-4 rounded-lg bg-green-500/10 border border-green-500/50 text-green-300">
            {success}
          </div>
        )}

        {/* Search & Filter Section */}
        <div className="mb-12 space-y-4">
          <div className="relative">
            <input
              type="text"
              placeholder="Anime durchsuchen..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              className="w-full px-6 py-4 rounded-xl bg-slate-800 border border-orange-500/30 text-white placeholder-gray-500 focus:outline-none focus:border-orange-500 focus:ring-2 focus:ring-orange-500/50 transition-all"
            />
            <span className="absolute right-4 top-1/2 -translate-y-1/2 text-gray-500">🔍</span>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-300 mb-2">Genre</label>
              <select
                value={selectedGenre}
                onChange={(e) => setSelectedGenre(e.target.value)}
                className="w-full px-4 py-3 rounded-lg bg-slate-800 border border-orange-500/30 text-white focus:outline-none focus:border-orange-500"
              >
                <option value="">Alle Genres</option>
                {genres.map(g => (
                  <option key={g} value={g}>{g}</option>
                ))}
              </select>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-300 mb-2">Status</label>
              <select
                value={selectedStatus}
                onChange={(e) => setSelectedStatus(e.target.value)}
                className="w-full px-4 py-3 rounded-lg bg-slate-800 border border-orange-500/30 text-white focus:outline-none focus:border-orange-500"
              >
                <option value="">Alle Status</option>
                {statuses.map(s => (
                  <option key={s} value={s}>
                    {s === 'AIRING' ? '📡 Läuft' : s === 'COMPLETED' ? '✅ Abgeschlossen' : '⏰ Geplant'}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-300 mb-2">Sortieren</label>
              <select
                value={sortBy}
                onChange={(e) => setSortBy(e.target.value)}
                className="w-full px-4 py-3 rounded-lg bg-slate-800 border border-orange-500/30 text-white focus:outline-none focus:border-orange-500"
              >
                <option value="title">Titel</option>
                <option value="year">Jahr (Neu)</option>
                <option value="rating">Rating</option>
                <option value="episodes">Episodes</option>
              </select>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-300 mb-2">Gefunden</label>
              <div className="px-4 py-3 rounded-lg bg-slate-700/50 text-orange-400 font-bold">
                {anime.length} Anime
              </div>
            </div>
          </div>
        </div>

        {/* Loading State */}
        {loading && (
          <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-6">
            {[...Array(10)].map((_, i) => (
              <div key={i} className="animate-pulse">
                <div className="bg-slate-700 rounded-lg h-64 mb-3"></div>
                <div className="bg-slate-700 rounded h-4 w-3/4 mb-2"></div>
                <div className="bg-slate-700 rounded h-3 w-1/2"></div>
              </div>
            ))}
          </div>
        )}

        {/* Anime Grid */}
        {!loading && (
          <>
            {anime.length === 0 ? (
              <div className="text-center py-16">
                <div className="text-6xl mb-4">🎬</div>
                <h3 className="text-2xl font-bold text-white mb-2">Keine Anime gefunden</h3>
                <p className="text-gray-400">Versuche andere Filter oder Suchbegriffe</p>
              </div>
            ) : (
              <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-6">
                {anime.map(a => (
                  <div key={a.id} className="group cursor-pointer">
                    <Link to={`/anime/${a.id}`} className="block mb-3 overflow-hidden rounded-lg shadow-lg relative">
                      <img
                        src={a.coverUrl || 'https://via.placeholder.com/300x400?text=No+Cover'}
                        alt={a.title}
                        className="w-full h-64 object-cover group-hover:scale-110 transition-transform duration-300"
                      />
                      {a.status && (
                        <div className="absolute top-2 right-2 bg-orange-500 text-white text-xs font-bold px-2 py-1 rounded-full">
                          {a.status === 'AIRING' ? '📡' : a.status === 'COMPLETED' ? '✅' : '⏰'}
                        </div>
                      )}
                      <div className="absolute inset-0 bg-gradient-to-t from-black/80 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-300 flex items-end p-3">
                        <button 
                          onClick={(e) => {
                            e.preventDefault()
                            handleAddToWatchlist(a.id)
                          }}
                          disabled={addingToWatchlist === a.id}
                          className="w-full bg-orange-500 hover:bg-orange-600 disabled:bg-gray-600 text-white font-bold py-2 rounded-lg transition-colors"
                        >
                          {addingToWatchlist === a.id ? '⏳' : '➕ Watchlist'}
                        </button>
                      </div>
                    </Link>

                    <h3 className="font-bold text-white text-sm line-clamp-2 group-hover:text-orange-400 transition-colors">
                      {a.title}
                    </h3>
                    <div className="flex items-center justify-between mt-2">
                      <span className="text-orange-400 font-bold text-sm">
                        {a.avgRating ? `⭐ ${a.avgRating.toFixed(1)}` : '⭐ N/A'}
                      </span>
                      {a.episodeCount && (
                        <span className="text-gray-400 text-xs">{a.episodeCount} Eps</span>
                      )}
                    </div>
                    {a.genre && (
                      <p className="text-gray-400 text-xs mt-1 line-clamp-1">{a.genre}</p>
                    )}
                  </div>
                ))}
              </div>
            )}
          </>
        )}
      </main>
    </div>
  )
}
