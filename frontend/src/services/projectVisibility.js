export function filterProjectsForUser(projects, currentUser) {
  if (currentUser.role === 'ADMIN') {
    return projects;
  }
  if (currentUser.role === 'PROJEKTLEITER') {
    return projects.filter((project) => project.projectLeadId === currentUser.id);
  }
  return projects.filter((project) => project.memberIds.includes(currentUser.id));
}
