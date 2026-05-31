import React, { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../api/api'

export default function WatchlistPage() {
  const [entries, setEntries] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [statusFilter, setStatusFilter] = useState('')

  useEffect(() => {
    loadWatchlist()
  }, [statusFilter])

  const loadWatchlist = async () => {
    setLoading(true)
    try {
      const data = await api.getWatchlist(0, 20, statusFilter)
      setEntries(data.content)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const handleRemove = async (id) => {
    try {
      await api.removeFromWatchlist(id)
      loadWatchlist()
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <div style={{ padding: '20px' }}>
      <h1>My Watchlist</h1>
      <Link to="/" style={{ marginBottom: '20px' }}>← Back to Anime</Link>

      <select value={statusFilter} onChange={(e) => setStatusFilter(e.target.value)} style={{ marginBottom: '20px' }}>
        <option value="">All Status</option>
        <option value="PLAN_TO_WATCH">Plan to Watch</option>
        <option value="WATCHING">Watching</option>
        <option value="COMPLETED">Completed</option>
        <option value="DROPPED">Dropped</option>
      </select>

      {error && <div style={{ color: 'red' }}>{error}</div>}
      {loading && <div>Loading...</div>}

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(250px, 1fr))', gap: '20px' }}>
        {entries.map(entry => (
          <div key={entry.id} style={{ border: '1px solid #ddd', padding: '15px', borderRadius: '8px' }}>
            <Link to={`/anime/${entry.anime.id}`} style={{ textDecoration: 'none', color: 'black' }}>
              <img src={entry.anime.coverUrl} alt={entry.anime.title} style={{ width: '100%', height: '200px', objectFit: 'cover' }} />
              <h3>{entry.anime.title}</h3>
            </Link>
            <p>Status: {entry.status}</p>
            <p>Rating: {entry.anime.avgRating || 'N/A'}/10</p>
            <button onClick={() => handleRemove(entry.id)}>Remove</button>
          </div>
        ))}
      </div>
    </div>
  )
}
