import { useState, useEffect } from 'react'
import ProfileForm from './components/ProfileForm'
import ProfileList from './components/ProfileList'
import './App.css'

function App() {
  const [profiles, setProfiles] = useState([])
  const [editingProfile, setEditingProfile] = useState(null)
  const [showForm, setShowForm] = useState(false)

  useEffect(() => {
    fetchProfiles()
  }, [])

  const fetchProfiles = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/profiles')
      const data = await response.json()
      setProfiles(data)
    } catch (error) {
      console.error('Error fetching profiles:', error)
    }
  }

  const handleCreate = () => {
    setEditingProfile(null)
    setShowForm(true)
  }

  const handleEdit = (profile) => {
    setEditingProfile(profile)
    setShowForm(true)
  }

  const handleFormClose = () => {
    setShowForm(false)
    setEditingProfile(null)
  }

  const handleFormSubmit = () => {
    fetchProfiles()
    handleFormClose()
  }

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this profile?')) {
      try {
        await fetch(`http://localhost:8080/api/profiles/${id}`, {
          method: 'DELETE'
        })
        fetchProfiles()
      } catch (error) {
        console.error('Error deleting profile:', error)
        alert('Error deleting profile')
      }
    }
  }

  return (
    <div className="app">
      <header className="app-header">
        <h1>User Profiles</h1>
        <button onClick={handleCreate} className="btn btn-primary">
          Create New Profile
        </button>
      </header>

      <main className="app-main">
        {showForm && (
          <ProfileForm
            profile={editingProfile}
            onSubmit={handleFormSubmit}
            onCancel={handleFormClose}
          />
        )}

        <ProfileList
          profiles={profiles}
          onEdit={handleEdit}
          onDelete={handleDelete}
        />
      </main>
    </div>
  )
}

export default App

