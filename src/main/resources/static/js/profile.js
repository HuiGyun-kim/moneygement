document.addEventListener('DOMContentLoaded', function() {
    const userId = document.getElementById('userId').getAttribute('data-user-id');
    updateExp(userId);
});

let currentExp = 0;
const baseExpPerLevel = 100; // 초기 레벨당 필요한 경험치
const maxLevel = 6; // 최대 레벨
const expIncreasePerLevel = 500; // 레벨이 올라갈 때마다 필요한 추가 경험치

// HTML 요소 가져오기
const progressBar = document.getElementById('progress-bar');
const currentLevelSpan = document.getElementById('current-level');
const filledExpSpan = document.getElementById('filled-exp');
const requiredExpSpan = document.getElementById('required-exp');

// 경험치 추가 및 레벨 업데이트 함수
function addExp(exp) {
    currentExp += exp;

    // 현재 레벨을 계산
    let currentLevel = 1;
    let requiredExp = getRequiredExpForLevel(currentLevel);

    // 다음 레벨로 넘어가는 조건 확인
    while (currentLevel < maxLevel && currentExp >= requiredExp) {
        currentLevel++;
        requiredExp = getRequiredExpForLevel(currentLevel);
    }

    // 최대 레벨에 도달한 경우 최대 필요 경험치로 설정
    if (currentLevel >= maxLevel) {
        currentExp = getRequiredExpForLevel(maxLevel);
    }

    // 경험치 및 레벨 정보 업데이트
    updateProgress(currentLevel);
}

// 레벨 막대 및 텍스트 업데이트 함수
function updateProgress(currentLevel) {
    const requiredExp = getRequiredExpForLevel(currentLevel);

    // 레벨 막대 업데이트
    const percent = (currentExp / requiredExp) * 100;
    progressBar.style.width = `${percent}%`;

    // HTML 요소에 정보 표시
    currentLevelSpan.textContent = currentLevel;
    filledExpSpan.textContent = currentExp;
    requiredExpSpan.textContent = requiredExp;

    console.log(`현재 레벨: ${currentLevel}`);
    console.log(`채운 경험치: ${currentExp} / 레벨당 필요한 경험치: ${requiredExp}`);

    // 최대 레벨 달성 시 텍스트 변경
    if (currentLevel >= maxLevel) {
        console.log(`최대 레벨(${maxLevel}) 달성!`);
    } else if (currentExp >= requiredExp) {
        console.log(`레벨 업! 현재 레벨: ${currentLevel}`);
    }
}

// 해당 레벨에 필요한 경험치를 반환하는 함수
function getRequiredExpForLevel(level) {
    return baseExpPerLevel + (level - 1) * expIncreasePerLevel;
}

function updateExp(userId) {
    fetch(`/level/exp/${userId}`)
        .then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error('네트워크 응답이 올바르지 않습니다.');
        })
        .then(data => {
            const {exp, level} = data;
            currentLevelSpan.textContent = level;
            filledExpSpan.textContent = exp;
            requiredExpSpan.textContent = getRequiredExpForLevel(level);

            const percent = (exp / getRequiredExpForLevel(level)) * 100;
            progressBar.style.width = `${percent}%`;
        })
        .catch((error) => {
            console.error('문제가 발생했습니다:', error);
        });
}

