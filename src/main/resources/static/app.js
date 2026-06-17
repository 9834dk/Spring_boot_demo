const API_BASE_URL = ''; // Relative path for Spring Boot
let currentUser = null;
let ageChartInstance = null;

document.addEventListener('DOMContentLoaded', () => {
    // Auth Check
    const userJson = localStorage.getItem('user');
    if (!userJson) {
        window.location.href = 'login.html';
        return;
    }
    currentUser = JSON.parse(userJson);
    document.getElementById('displayUsername').textContent = currentUser.username;
    
    // Fill Settings
    document.getElementById('setCurrUsername').value = currentUser.username;
    if(currentUser.email) document.getElementById('setCurrEmail').value = currentUser.email;

    // SPA Routing
    document.querySelectorAll('.nav-item[data-target]').forEach(item => {
        item.addEventListener('click', (e) => {
            e.preventDefault();
            document.querySelectorAll('.nav-item').forEach(n => n.classList.remove('active'));
            item.classList.add('active');
            
            document.querySelectorAll('.page-section').forEach(s => s.style.display = 'none');
            
            const target = item.getAttribute('data-target');
            document.getElementById(target).style.display = 'flex';
            
            if (target === 'analyticsSection') {
                renderAnalytics();
            }
        });
    });

    fetchStudents();
    
    document.getElementById('studentForm').addEventListener('submit', handleFormSubmit);
    document.getElementById('profileForm').addEventListener('submit', handleProfileUpdate);
    document.getElementById('passwordForm').addEventListener('submit', handlePasswordUpdate);
});

function logout() {
    localStorage.removeItem('user');
    window.location.href = 'login.html';
}

// Data State
let studentsData = [];

async function fetchStudents() {
    renderSkeletons();
    try {
        const response = await fetch(`${API_BASE_URL}/students`);
        const result = await response.json();
        
        if (result.success) {
            studentsData = result.data || [];
            renderStudents(studentsData);
        } else {
            showToast('Failed to load students', 'error');
            renderEmptyState();
        }
    } catch (error) {
        console.error('Error fetching students:', error);
        showToast('Network error while loading data', 'error');
        renderEmptyState();
    }
}

function renderSkeletons() {
    const tableBody = document.getElementById('studentTableBody');
    tableBody.innerHTML = '';
    for (let i = 0; i < 3; i++) {
        tableBody.innerHTML += `
            <tr>
                <td><div class="skeleton" style="width: 30px;"></div></td>
                <td><div class="skeleton" style="width: 150px;"></div></td>
                <td><div class="skeleton" style="width: 40px;"></div></td>
                <td><div class="skeleton" style="width: 60px;"></div></td>
                <td><div class="skeleton" style="width: 80px;"></div></td>
                <td><div class="skeleton" style="width: 100px;"></div></td>
                <td><div class="skeleton" style="width: 200px;"></div></td>
                <td><div class="skeleton" style="width: 100px;"></div></td>
            </tr>
        `;
    }
}

function renderEmptyState() {
    document.getElementById('studentTableBody').innerHTML = `
        <tr>
            <td colspan="8" style="text-align: center; padding: 60px; color: var(--text-secondary);">
                <i class="fa-solid fa-folder-open" style="font-size: 3rem; margin-bottom: 15px; opacity: 0.5;"></i>
                <p>No students found. Add one to get started!</p>
            </td>
        </tr>
    `;
}

function renderStudents(students) {
    const tableBody = document.getElementById('studentTableBody');
    tableBody.innerHTML = '';
    
    if (!students || students.length === 0) {
        renderEmptyState();
        return;
    }

    students.forEach((student, index) => {
        const tr = document.createElement('tr');
        tr.style.opacity = '0';
        tr.style.animation = `fadeInRow 0.4s ease forwards ${index * 0.05}s`;
        
        const ageDisplay = student.age ? student.age : '-';
        
        tr.innerHTML = `
            <td>#${student.id}</td>
            <td style="font-weight: 600;">${student.name}</td>
            <td>${ageDisplay}</td>
            <td>${student.gender || '-'}</td>
            <td>${student.grade || '-'}</td>
            <td>${student.hobby || '-'}</td>
            <td><a href="mailto:${student.email}" style="color: var(--accent); text-decoration: none;">${student.email}</a></td>
            <td>
                <div class="actions">
                    <button class="btn btn-secondary btn-sm" onclick="editStudent(${student.id}, '${student.name}', '${student.email}', ${student.age || 'null'}, '${student.gender || ''}', '${student.hobby || ''}', '${student.grade || ''}')">
                        <i class="fa-solid fa-pen"></i> Edit
                    </button>
                    <button class="btn btn-danger btn-sm" onclick="deleteStudent(${student.id})">
                        <i class="fa-solid fa-trash"></i> Delete
                    </button>
                </div>
            </td>
        `;
        tableBody.appendChild(tr);
    });
}

let isEditing = false;
const modal = document.getElementById('studentModal');
const form = document.getElementById('studentForm');

function openModal() {
    isEditing = false;
    form.reset();
    document.getElementById('studentId').value = '';
    document.getElementById('modalTitle').textContent = 'Add New Student';
    modal.classList.add('active');
}

function closeModal() {
    modal.classList.remove('active');
}

function editStudent(id, name, email, age, gender, hobby, grade) {
    isEditing = true;
    document.getElementById('studentId').value = id;
    document.getElementById('studentName').value = name;
    document.getElementById('studentEmail').value = email;
    document.getElementById('studentAge').value = age || '';
    document.getElementById('studentGender').value = gender || '';
    document.getElementById('studentHobby').value = hobby || '';
    document.getElementById('studentGrade').value = grade || '';
    document.getElementById('modalTitle').textContent = 'Edit Student Details';
    modal.classList.add('active');
}

async function handleFormSubmit(e) {
    e.preventDefault();
    
    const id = document.getElementById('studentId').value;
    const name = document.getElementById('studentName').value;
    const email = document.getElementById('studentEmail').value;
    const age = document.getElementById('studentAge').value;
    const gender = document.getElementById('studentGender').value;
    const hobby = document.getElementById('studentHobby').value;
    const grade = document.getElementById('studentGrade').value;
    
    const submitBtn = form.querySelector('button[type="submit"]');
    const originalText = submitBtn.innerHTML;
    submitBtn.innerHTML = '<i class="fa-solid fa-circle-notch fa-spin"></i> Saving...';
    submitBtn.disabled = true;

    try {
        if (isEditing) {
            const url = new URL(`${window.location.origin}${API_BASE_URL}/student/${id}`);
            url.searchParams.append('name', name);
            url.searchParams.append('email', email);
            if(age) url.searchParams.append('age', age);
            if(gender) url.searchParams.append('gender', gender);
            if(hobby) url.searchParams.append('hobby', hobby);
            if(grade) url.searchParams.append('grade', grade);
            
            const response = await fetch(url, { method: 'PUT' });
            const result = await response.json();
            
            if (result.success) {
                showToast('Student updated successfully!', 'success');
                closeModal();
                fetchStudents();
            } else {
                showToast(result.errorMsg || 'Failed to update student', 'error');
            }
        } else {
            const bodyData = { name, email, gender, hobby, grade };
            if(age) bodyData.age = parseInt(age);
            
            const response = await fetch(`${API_BASE_URL}/student`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(bodyData)
            });
            const result = await response.json();
            
            if (result.success) {
                showToast('Student added successfully!', 'success');
                closeModal();
                fetchStudents();
            } else {
                showToast(result.errorMsg || 'Failed to add student', 'error');
            }
        }
    } catch (error) {
        showToast('Network error occurred', 'error');
    } finally {
        submitBtn.innerHTML = originalText;
        submitBtn.disabled = false;
    }
}

async function deleteStudent(id) {
    if (!confirm('Are you sure you want to delete this student?')) return;
    
    try {
        const response = await fetch(`${API_BASE_URL}/student/${id}`, { method: 'DELETE' });
        const result = await response.json();
        
        if (result.success) {
            showToast('Student deleted successfully!', 'success');
            fetchStudents();
        } else {
            showToast(result.errorMsg || 'Failed to delete student', 'error');
        }
    } catch (error) {
        showToast('Network error occurred', 'error');
    }
}

// Settings
async function handleProfileUpdate(e) {
    e.preventDefault();
    const newEmail = document.getElementById('setCurrEmail').value;
    try {
        const url = new URL(`${window.location.origin}${API_BASE_URL}/user/${currentUser.id}`);
        url.searchParams.append('email', newEmail);
        const res = await fetch(url, { method: 'PUT' });
        const data = await res.json();
        if(data.success) {
            currentUser = data.data;
            localStorage.setItem('user', JSON.stringify(currentUser));
            showToast('Profile updated!', 'success');
        } else {
            showToast(data.errorMsg || 'Update failed', 'error');
        }
    } catch(err) {
        showToast('Network error', 'error');
    }
}

async function handlePasswordUpdate(e) {
    e.preventDefault();
    const oldP = document.getElementById('setOldPassword').value;
    const newP = document.getElementById('setNewPassword').value;
    try {
        const url = new URL(`${window.location.origin}${API_BASE_URL}/user/${currentUser.id}/password`);
        url.searchParams.append('oldPassword', oldP);
        url.searchParams.append('newPassword', newP);
        const res = await fetch(url, { method: 'PUT' });
        const data = await res.json();
        if(data.success) {
            showToast('Password changed!', 'success');
            document.getElementById('passwordForm').reset();
        } else {
            showToast(data.errorMsg || 'Failed to change password', 'error');
        }
    } catch(err) {
        showToast('Network error', 'error');
    }
}

// Analytics
function renderAnalytics() {
    document.getElementById('statTotal').textContent = studentsData.length;
    
    let totalAge = 0;
    let countAge = 0;
    
    let ageGroups = {
        'Under 18': 0,
        '18-22': 0,
        '23-26': 0,
        'Over 26': 0
    };

    studentsData.forEach(s => {
        if(s.age) {
            totalAge += s.age;
            countAge++;
            
            if(s.age < 18) ageGroups['Under 18']++;
            else if(s.age <= 22) ageGroups['18-22']++;
            else if(s.age <= 26) ageGroups['23-26']++;
            else ageGroups['Over 26']++;
        }
    });

    const avgAge = countAge > 0 ? (totalAge / countAge).toFixed(1) : 0;
    document.getElementById('statAvgAge').textContent = avgAge;

    const ctx = document.getElementById('ageDistributionChart').getContext('2d');
    
    if (ageChartInstance) {
        ageChartInstance.destroy();
    }

    ageChartInstance = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: Object.keys(ageGroups),
            datasets: [{
                data: Object.values(ageGroups),
                backgroundColor: [
                    'rgba(139, 92, 246, 0.8)',
                    'rgba(59, 130, 246, 0.8)',
                    'rgba(16, 185, 129, 0.8)',
                    'rgba(245, 158, 11, 0.8)'
                ],
                borderColor: 'rgba(255,255,255,0.1)',
                borderWidth: 2,
                hoverOffset: 4
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'bottom',
                    labels: { color: '#ffffff' }
                },
                title: {
                    display: true,
                    text: 'Student Age Distribution',
                    color: '#ffffff',
                    font: { size: 18, family: 'Inter' }
                }
            }
        }
    });
}

function showToast(message, type = 'success') {
    const existing = document.querySelector('.toast');
    if (existing) existing.remove();
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    const icon = type === 'success' ? 'fa-circle-check' : 'fa-circle-exclamation';
    toast.innerHTML = `<i class="fa-solid ${icon}"></i><span>${message}</span>`;
    document.body.appendChild(toast);
    setTimeout(() => toast.classList.add('show'), 10);
    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}
