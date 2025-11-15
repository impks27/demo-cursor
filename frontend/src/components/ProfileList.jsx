import './ProfileList.css'

const ProfileList = ({ profiles, onEdit, onDelete }) => {
  if (profiles.length === 0) {
    return (
      <div className="profile-list-empty">
        <p>No profiles found. Create your first profile!</p>
      </div>
    )
  }

  return (
    <div className="profile-list">
      {profiles.map(profile => (
        <div key={profile.id} className="profile-card">
          <div className="profile-header">
            {profile.avatarUrl && (
              <img 
                src={profile.avatarUrl} 
                alt={profile.name}
                className="profile-avatar"
                onError={(e) => {
                  e.target.style.display = 'none'
                }}
              />
            )}
            <div className="profile-info">
              <h3>{profile.name}</h3>
              <p className="profile-email">{profile.email}</p>
              {profile.location && (
                <p className="profile-location">📍 {profile.location}</p>
              )}
            </div>
          </div>
          
          {profile.bio && (
            <p className="profile-bio">{profile.bio}</p>
          )}
          
          <div className="profile-details">
            {profile.phone && (
              <div className="profile-detail-item">
                <span className="detail-label">Phone:</span>
                <span>{profile.phone}</span>
              </div>
            )}
            {profile.website && (
              <div className="profile-detail-item">
                <span className="detail-label">Website:</span>
                <a href={profile.website} target="_blank" rel="noopener noreferrer">
                  {profile.website}
                </a>
              </div>
            )}
          </div>
          
          <div className="profile-footer">
            <div className="profile-meta">
              <small>Created: {new Date(profile.createdAt).toLocaleDateString()}</small>
            </div>
            <div className="profile-actions">
              <button 
                onClick={() => onEdit(profile)}
                className="btn btn-secondary btn-sm"
              >
                Edit
              </button>
              <button 
                onClick={() => onDelete(profile.id)}
                className="btn btn-danger btn-sm"
              >
                Delete
              </button>
            </div>
          </div>
        </div>
      ))}
    </div>
  )
}

export default ProfileList

