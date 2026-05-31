import React, { useState, useEffect } from 'react'
import { api } from '../api/api'

export default function AdminAnimeManagerPage() {
  const [anime, setAnime] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [editingId, setEditingId] = useState(null)
  const [formData, setFormData] = useState({
    title: '', description: '', coverUrl: '', genre: '', episodeCount: 0, status: 'AIRING', releaseYear: new Date().getFullYear()
  })

  useEffect(() => {
    loadAnime()
  }, [])

  const loadAnime = async () => {
    setLoading(true)
    try {
      const data = await api.getAnime(0, 100)
      setAnime(Array.isArray(data) ? data : data?.content || [])
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const handleEdit = (item) => {
    setEditingId(item.id)
    setFormData({
      title: item.title,
      description: item.description,
      coverUrl: item.coverUrl,
      genre: item.genre,
      episodeCount: item.episodeCount || 0,
      status: item.status,
      releaseYear: item.releaseYear || new Date().getFullYear()
    })
  }

  const handleSave = async () => {
    setLoading(true)
    try {
      await api.updateAnime(editingId, formData)
      setError('')
      setEditingId(null)
      await loadAnime()
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const handleDelete = async (id) => {
    if (!confirm('Wirklich löschen?')) return
    setLoading(true)
    try {
      await api.deleteAnime(id)
      await loadAnime()
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="p-8 bg-slate-900 min-h-screen">
      <h2 className="text-3xl font-bold text-white mb-8">🎬 Anime Manager</h2>

      {error && <div className="mb-6 p-4 bg-red-500/10 border border-red-500/50 text-red-300 rounded-lg">{error}</div>}

      {loading && <div className="text-gray-400">Lädt...</div>}

      {!loading && (
        <div className="overflow-x-auto">
          <table className="w-full text-left text-gray-300 bg-slate-800 rounded-lg overflow-hidden">
            <thead className="bg-slate-700 border-b border-orange-500/20">
              <tr>
                <th className="px-6 py-4">Titel</th>
                <th className="px-6 py-4">Status</th>
                <th className="px-6 py-4">Episodes</th>
                <th className="px-6 py-4">Genre</th>
                <th className="px-6 py-4">Aktionen</th>
              </tr>
            </thead>
            <tbody>
              {anime.map(item => (
                <tr key={item.id} className="border-b border-slate-700 hover:bg-slate-700/50 transition-colors">
                  <td className="px-6 py-4 font-bold text-white">{item.title}</td>
                  <td className="px-6 py-4">
                    <span className={`px-3 py-1 rounded-full text-xs font-bold ${
                      item.status === 'AIRING' ? 'bg-green-500/20 text-green-400' :
                      item.status === 'COMPLETED' ? 'bg-blue-500/20 text-blue-400' :
                      'bg-yellow-500/20 text-yellow-400'
                    }`}>
                      {item.status === 'AIRING' ? '📡 Läuft' : item.status === 'COMPLETED' ? '✅ Fertig' : '⏰ Geplant'}
                    </span>
                  </td>
                  <td className="px-6 py-4">{item.episodeCount}</td>
                  <td className="px-6 py-4 text-sm">{item.genre}</td>
                  <td className="px-6 py-4 flex gap-2">
                    <button
                      onClick={() => handleEdit(item)}
                      className="px-3 py-1 bg-orange-500/20 text-orange-400 rounded hover:bg-orange-500/30 transition-colors text-sm"
                    >
                      ✏️ Edit
                    </button>
                    <button
                      onClick={() => handleDelete(item.id)}
                      className="px-3 py-1 bg-red-500/20 text-red-400 rounded hover:bg-red-500/30 transition-colors text-sm"
                    >
                      🗑️ Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {editingId && (
        <div className="fixed inset-0 bg-black/80 flex items-center justify-center p-4 z-50">
          <div className="bg-slate-800 rounded-xl border border-orange-500/20 p-8 max-w-2xl w-full">
            <h3 className="text-2xl font-bold text-white mb-6">Edit Anime</h3>

            <div className="space-y-4">
              <input
                type="text"
                value={formData.title}
                onChange={(e) => setFormData({...formData, title: e.target.value})}
                placeholder="Titel"
                className="w-full px-4 py-2 rounded-lg bg-slate-700 border border-orange-500/30 text-white"
              />
              <textarea
                value={formData.description}
                onChange={(e) => setFormData({...formData, description: e.target.value})}
                placeholder="Beschreibung"
                className="w-full px-4 py-2 rounded-lg bg-slate-700 border border-orange-500/30 text-white resize-none"
                rows="3"
              />
              <input
                type="url"
                value={formData.coverUrl}
                onChange={(e) => setFormData({...formData, coverUrl: e.target.value})}
                placeholder="Cover URL"
                className="w-full px-4 py-2 rounded-lg bg-slate-700 border border-orange-500/30 text-white"
              />
              <input
                type="text"
                value={formData.genre}
                onChange={(e) => setFormData({...formData, genre: e.target.value})}
                placeholder="Genre"
                className="w-full px-4 py-2 rounded-lg bg-slate-700 border border-orange-500/30 text-white"
              />
              <div className="grid grid-cols-3 gap-4">
                <input
                  type="number"
                  value={formData.episodeCount}
                  onChange={(e) => setFormData({...formData, episodeCount: parseInt(e.target.value)})}
                  placeholder="Episodes"
                  className="px-4 py-2 rounded-lg bg-slate-700 border border-orange-500/30 text-white"
                />
                <select
                  value={formData.status}
                  onChange={(e) => setFormData({...formData, status: e.target.value})}
                  className="px-4 py-2 rounded-lg bg-slate-700 border border-orange-500/30 text-white"
                >
                  <option value="AIRING">📡 Läuft</option>
                  <option value="COMPLETED">✅ Fertig</option>
                  <option value="UPCOMING">⏰ Geplant</option>
                </select>
                <input
                  type="number"
                  value={formData.releaseYear}
                  onChange={(e) => setFormData({...formData, releaseYear: parseInt(e.target.value)})}
                  placeholder="Jahr"
                  className="px-4 py-2 rounded-lg bg-slate-700 border border-orange-500/30 text-white"
                />
              </div>
            </div>

            <div className="flex gap-4 mt-8">
              <button
                onClick={handleSave}
                className="flex-1 bg-gradient-to-r from-orange-500 to-red-600 hover:from-orange-600 hover:to-red-700 text-white font-bold py-2 rounded-lg"
              >
                ✅ Speichern
              </button>
              <button
                onClick={() => setEditingId(null)}
                className="flex-1 bg-slate-700 hover:bg-slate-600 text-white font-bold py-2 rounded-lg"
              >
                ❌ Abbrechen
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
